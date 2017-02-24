package hu.schonherz.project.admin.web.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import org.primefaces.model.DualListModel;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.util.EmailUtils;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import hu.schonherz.project.admin.web.view.security.SecurityManagerBean;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "companyProfileView")
@ViewScoped
@Data
@Slf4j
public class CompanyProfileView {

    private static final String SUCCESSFUL_CHANGING = "success_profile_changes";
    private static final String AGENTS_COMP_ID = "profileForm:agentsPicklist";
    private static final String EMAIL_COMP_ID = "profileForm:email";
    private static final String ERROR_ADMIN_EMAIL = "error_admin_email";
    private static final String ERROR_MODIFY_PROFILE = "error_modify_profile";

    private static final Lock LOCK;

    static {
        LOCK = new ReentrantLock();
    }

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @ManagedProperty(value = "#{securityManagerBean}")
    private SecurityManagerBean securityManager;

    @EJB
    private UserServiceRemote userServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;
    @EJB
    private EmailUtils emailUtils;

    private CompanyVo currentCompanyVo;

    private CompanyForm companyProfileForm;

    private DualListModel<String> agents;

    @PostConstruct
    public void init() {
        UserVo loggedInUser = securityManager.getLoggedInUser();
        FacesContext context = FacesContext.getCurrentInstance();
        String companyIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
        if (companyIdParameter == null) {
            if (!securityManager.isUserCompanyAdmin()) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_LIST);
            } else {
                Long companyId = companyServiceRemote.findByName(loggedInUser.getCompanyName()).getId();
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        } else if (loggedInUser.getUserRole() == UserRole.COMPANY_ADMIN) {
            Long companyId = companyServiceRemote.findByName(loggedInUser.getCompanyName()).getId();
            if (!companyId.equals(Long.valueOf(companyIdParameter))) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        }
        currentCompanyVo = companyServiceRemote.findById(Long.valueOf(companyIdParameter));
        companyProfileForm = new CompanyForm(currentCompanyVo);
        initDualistModel();
    }

    public List<String> completeEmail(final String emailPart) {
        return emailUtils.completeEmailForProfile(emailPart, currentCompanyVo.getCompanyName());
    }

    private void initDualistModel() {
        agents = new DualListModel<>(agentsSource(), agentsTarget());
    }

    private List<String> agentsSource() {
        // A user can be hired if he has no company yet or he work at the current company but not as an agent.
        Predicate<UserVo> canHireUser = user -> {
            if (user.getCompanyName() == null) {
                return true;
            }
            if (!user.getCompanyName().equals(currentCompanyVo.getCompanyName())) {
                return false;
            }

            return !currentCompanyVo.getAgents().contains(user);
        };
        return userServiceRemote.findAll().stream()
                .filter(canHireUser)
                .map(UserVo::getUsername)
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> agentsTarget() {
        List<String> usernames = new ArrayList<>();
        for (UserVo userVo : currentCompanyVo.getAgents()) {
            usernames.add(userVo.getUsername());
        }
        return usernames;
    }

    public void save() {
        try {
            LOCK.lock();

            if (!setNewCompanyAdminIfGiven() || !saveCompanyAndNotify()) {
                return;
            }

            // Update company name if changed
            String newCompanyName = companyProfileForm.getCompanyName();
            String oldCompanyName = currentCompanyVo.getCompanyName();
            if (!oldCompanyName.equals(newCompanyName)) {
                log.debug("Company name changed from {0} to {1}. Modifiing employees.", oldCompanyName, newCompanyName);
                currentCompanyVo.setCompanyName(newCompanyName);
                // Modify company name for all employees too
                currentCompanyVo.getAgents().forEach(agent -> agent.setCompanyName(newCompanyName));
                userServiceRemote.saveAll(currentCompanyVo.getAgents());
            }

            // Update quotas
            QuotasVo companyQuotas = currentCompanyVo.getQuotas();
            QuotasVo newQuotas = companyProfileForm.getQuotes();
            companyQuotas.setMaxDayTickets(newQuotas.getMaxDayTickets());
            companyQuotas.setMaxLoggedIn(newQuotas.getMaxLoggedIn());
            companyQuotas.setMaxMonthTickets(newQuotas.getMaxMonthTickets());
            companyQuotas.setMaxUsers(newQuotas.getMaxUsers());
            companyQuotas.setMaxWeekTickets(newQuotas.getMaxWeekTickets());

            updateCompanyAgentsIFChanged();

            saveCompanyAndNotify();
            initDualistModel();
        } finally {
            LOCK.unlock();
        }
    }

    private void updateCompanyAgentsIFChanged() {
        Set<String> currentAgentUsernames = currentCompanyVo.getAgents().stream()
                .map(UserVo::getUsername)
                .collect(Collectors.toSet());

        List<String> updatedAgentUsernames = agents.getTarget();

        Set<String> removedAgentUsernames = currentAgentUsernames.stream()
                .filter(agentUsername -> !updatedAgentUsernames.contains(agentUsername))
                .collect(Collectors.toSet());
        Set<String> addedAgentUsernames = updatedAgentUsernames.stream()
                .filter(agentUsername -> !currentAgentUsernames.contains(agentUsername))
                .collect(Collectors.toSet());

        // Set new state of changed agents if there are any
        updateAgentCollection(removedAgentUsernames, null);
        updateAgentCollection(addedAgentUsernames, currentCompanyVo.getCompanyName());
    }

    private void updateAgentCollection(@NonNull final Collection<String> changedAgentUsernames, final String newCompanyName) {
        if (!changedAgentUsernames.isEmpty()) {
            Set<UserVo> allChangedAgents = new HashSet<>();
            // Set new state (set company name) for each added agent
            changedAgentUsernames.forEach(changedAgentUsername -> {
                UserVo changedAgent = userServiceRemote.findByUsername(changedAgentUsername);
                if (!changedAgent.getEmail().equals(currentCompanyVo.getAdminEmail())) {
                    changedAgent.setCompanyName(newCompanyName);    // null if removed, current company's name if added
                }
                allChangedAgents.add(changedAgent);
            });

            userServiceRemote.saveAll(allChangedAgents);

            // Apply the changes to the agent set in the company vo
            Set<UserVo> companyAgents = currentCompanyVo.getAgents();
            if (newCompanyName != null) {
                companyAgents.addAll(allChangedAgents);
            } else {
                companyAgents = companyAgents.stream()
                        .filter(agent -> !changedAgentUsernames.contains(agent.getUsername()))
                        .collect(Collectors.toSet());
                currentCompanyVo.setAgents(companyAgents);
            }
        }
    }

    private boolean saveCompanyAndNotify() {
        // Save company and notify user
        try {
            currentCompanyVo = companyServiceRemote.save(currentCompanyVo);
            localeManagerBean.sendMessage(AGENTS_COMP_ID, FacesMessage.SEVERITY_INFO, SUCCESSFUL_CHANGING);
            log.info("Company profile '{}' successfully changed.", currentCompanyVo.getCompanyName());
            return true;
        } catch (InvalidCompanyDataException icde) {
            String lnSep = System.getProperty("line.separator");
            localeManagerBean.sendMessage(AGENTS_COMP_ID, FacesMessage.SEVERITY_ERROR, ERROR_MODIFY_PROFILE);
            log.warn("Unsuccessful changing attempt with data:{}{} ", lnSep, companyProfileForm);
            log.warn("Causing exception:" + lnSep, icde);
            return false;
        }
    }

    private boolean setNewCompanyAdminIfGiven() {
        String oldAdminEmail = currentCompanyVo.getAdminEmail();
        String newAdminEmail = companyProfileForm.getAdminEmail();
        // If company admin has changed
        if (!newAdminEmail.equals(oldAdminEmail)) {
            // Load both user vos
            UserVo oldAdmin = userServiceRemote.findByEmail(oldAdminEmail);
            UserVo newAdmin = userServiceRemote.findByEmail(newAdminEmail);
            if (oldAdmin == null || newAdmin == null) {
                log.error("Could not read both old ({}) and new ({}) company admins from the database!", oldAdmin, newAdminEmail);
                localeManagerBean.sendMessage(EMAIL_COMP_ID, FacesMessage.SEVERITY_ERROR, ERROR_ADMIN_EMAIL);
                return false;
            }

            // Apply state changes to old admin
            if (oldAdmin.getUserRole() != UserRole.ADMIN) {
                oldAdmin.setUserRole(UserRole.AGENT);
            }
            Set<UserVo> companyAgents = currentCompanyVo.getAgents();
            Optional<UserVo> adminAgent = companyAgents.stream()
                    .filter(agent -> agent.getEmail().equals(oldAdminEmail))
                    .findAny();
            if (adminAgent.isPresent()) {
                // Update the instance in the agents set too
                adminAgent.get().setUserRole(oldAdmin.getUserRole());
            } else {
                oldAdmin.setCompanyName(null);
            }

            // Apply state changes to new admin
            if (newAdmin.getUserRole() != UserRole.ADMIN) {
                newAdmin.setUserRole(UserRole.COMPANY_ADMIN);
            }
            if (newAdmin.getCompanyName() == null) {
                newAdmin.setCompanyName(currentCompanyVo.getCompanyName());
            }

            currentCompanyVo.setAdminEmail(newAdminEmail);

            try {
                userServiceRemote.saveAll(Arrays.asList(oldAdmin, newAdmin));
            } catch (Exception e) {
                log.error("Failed to switch company admin for company: " + currentCompanyVo.getCompanyName(), e);
                return false;
            }
        }

        return true;
    }

    /*
    private void setNewCompanyAdminIfGiven(CompanyVo companyVo) {
        UserVo newCompAdmin = userServiceRemote.findByEmail(companyProfileForm.getAdminEmail());
        UserVo oldCompAdmin = companyVo.getAdminUser();
        // if company admin changed
        if (!oldCompAdmin.getEmail().equals(newCompAdmin.getEmail())) {
            newCompAdmin.setUserRole(UserRole.COMPANY_ADMIN);
            oldCompAdmin.setUserRole(UserRole.AGENT);
            oldCompAdmin.setCompanyName(null);

            try {
                userServiceRemote.registrationUser(newCompAdmin);
                userServiceRemote.registrationUser(oldCompAdmin);
                companyVo.setAdminUser(newCompAdmin);
            } catch (InvalidUserDataException iude) {
                log.error("Could not change company admin for company " + companyVo.getCompanyName(), iude);
            }

            // Update dual list model
            List<String> source = agents.getSource();
            List<String> target = agents.getTarget();
            String newCompAdminName = newCompAdmin.getUsername();
            String oldCompAdminName = oldCompAdmin.getUsername();
            source.remove(newCompAdminName);
            target.remove(newCompAdminName);
            if (!source.contains(oldCompAdminName)) {
                source.add(oldCompAdminName);
            }
        }
    }
     */
}
