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
import javax.servlet.http.HttpSession;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import org.primefaces.model.DualListModel;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import java.util.Collection;
import java.util.Set;
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
    private static final String SUCCESS = "success_short";
    private static final String AGENTS_COMP_ID = "profileForm:agentsPicklist";
    private static final String EMAIL_COMP_ID = "profileForm:email";
    private static final String FAILURE = "error_failure_short";
    private static final String ERROR_ADMIN_EMAIL = "error_admin_email";

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private UserServiceRemote userServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;

    private CompanyVo currentCompanyVo;

    private CompanyForm companyProfileForm;

    private DualListModel<String> agents;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserVo userVo = getLoggedInUser(context);
        String companyIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
        if (companyIdParameter == null) {
            if (userVo.getUserRole().equals(UserRole.ADMIN)) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_LIST);
            } else {
                Long companyId = companyServiceRemote.findByName(userVo.getCompanyName()).getId();
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        } else if (userVo.getUserRole().equals(UserRole.COMPANY_ADMIN)) {
            Long companyId = companyServiceRemote.findByName(userVo.getCompanyName()).getId();
            if (!companyId.equals(Long.valueOf(companyIdParameter))) {
                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyId);
            }
        }
        currentCompanyVo = companyServiceRemote.findById(Long.valueOf(companyIdParameter));
        companyProfileForm = new CompanyForm(currentCompanyVo);
        initDualistModel();
    }

//    Completing the email list for the email form
    public List<String> completeEmail(final String query) {
        List<String> emails = new ArrayList<>();
        for (UserVo userVo : userServiceRemote.findAll()) {
            if (userVo.getEmail().contains(query)) {
                emails.add(userVo.getEmail());
            }
        }
        return emails;
    }

    private void initDualistModel() {
        agents = new DualListModel<>(agentsSource(), agentsTarget());
    }

    private List<String> agentsSource() {
        List<String> usernames = new ArrayList<>();
        for (UserVo userVo : userServiceRemote.findAll()) {
            if (isAgent(userVo) && isIndependentUser(userVo)) {
                usernames.add(userVo.getUsername());
            }
        }
        return usernames;
    }

    private List<String> agentsTarget() {
        List<String> usernames = new ArrayList<>();
        for (UserVo userVo : currentCompanyVo.getAgents()) {
            usernames.add(userVo.getUsername());
        }
        return usernames;
    }

    private boolean isAgent(final UserVo userVo) {
        return userVo.getUserRole().equals(UserRole.AGENT);
    }

    private boolean isIndependentUser(final UserVo userVo) {
        for (CompanyVo companyVo : companyServiceRemote.findAll()) {
            if (companyVo.getCompanyName().equals(userVo.getCompanyName())) {
                return false;
            }
        }
        return true;
    }

    public void save() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (userServiceRemote.findByEmail(companyProfileForm.getAdminEmail()) == null) {
            context.addMessage(EMAIL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(ERROR_ADMIN_EMAIL)));
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
        QuotasVo companyQuotas = currentCompanyVo.getQuotes();
        QuotasVo newQuotas = companyProfileForm.getQuotes();
        companyQuotas.setMaxDayTickets(newQuotas.getMaxDayTickets());
        companyQuotas.setMaxLoggedIn(newQuotas.getMaxLoggedIn());
        companyQuotas.setMaxMonthTickets(newQuotas.getMaxMonthTickets());
        companyQuotas.setMaxUsers(newQuotas.getMaxUsers());
        companyQuotas.setMaxWeekTickets(newQuotas.getMaxWeekTickets());

        // Update agents and agent set
        updateCompanyAgentsIFChanged();

        CompanyVo companyVo = companyProfileForm.getCompanyVo();

        // Save company and notify user
        try {
            currentCompanyVo = companyServiceRemote.save(companyVo);
            sendMessage(AGENTS_COMP_ID, FacesMessage.SEVERITY_INFO, SUCCESSFUL_CHANGING);
            log.info("Company profile '{}' successfully changed.", companyVo.getCompanyName());
        } catch (InvalidCompanyDataException icde) {
            String lnSep = System.getProperty("line.separator");
            log.warn("Unsuccessful changing attempt with data:{}{} ", lnSep, companyProfileForm);
            log.warn("Causing exception:" + lnSep, icde);
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
                changedAgent.setCompanyName(newCompanyName);    // null if removed, current company's name if added
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

    private UserVo getLoggedInUser(FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }

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

    private void sendMessage(final String compId, final FacesMessage.Severity severity, final String detailedKey) {
        String localizedShort = severity == FacesMessage.SEVERITY_INFO ? localeManagerBean.localize(SUCCESS)
                : localeManagerBean.localize(FAILURE);
        String localizedDetailed = localeManagerBean.localize(detailedKey);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(compId, new FacesMessage(severity, localizedShort, localizedDetailed));
    }

}
