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
import org.primefaces.model.DualListModel;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "companyProfileView")
@ViewScoped
@Data
@Slf4j
public class CompanyProfileView {

    private static final String SUCCESSFUL_CHANGING = "success_profile_changes";
    private static final String SUCCESS = "success_short";
    private static final String GLOBAL_COMP_ID = "companyRegistrationForm";
    private static final String EMAIL_COMP_ID = "companyRegistrationForm:email";
    private static final String FAILURE = "error_failure_short";
    private static final String ERROR_ADMIN_EMAIL = "error_admin_email";

    private CompanyForm companyRegistrationForm;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

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
        String companyIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
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

        CompanyVo companyVo = companyProfileForm.getCompanyVo();
        companyVo.setActive(currentCompanyVo.isActive());
        HashSet<UserVo> userVos = new HashSet<>();
        for (String username : agents.getTarget()) {
            userVos.add(userServiceRemote.findByUsername(username));
        }
        companyVo.setAgents(userVos);
        try {
            companyServiceRemote.save(companyVo);
            currentCompanyVo = companyVo;

            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localeManagerBean.localize(SUCCESS), localeManagerBean.localize(SUCCESSFUL_CHANGING)));

            log.info("Company profile '{}' successfully changed.", companyVo.getCompanyName());
        } catch (InvalidCompanyDataException icde) {
            // TODO Auto-generated catch block
            icde.printStackTrace();
        }

    }

}
