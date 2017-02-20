package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.QuotasVo;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.CompanyForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.faces.context.FacesContext;

@ManagedBean(name = "companyRegistrationView")
@ViewScoped
@Data
@Slf4j
public class CompanyRegistrationView {

    private static final String SUCCESS = "success_short";
    private static final String SUCCESSFUL_REGISTRATION = "success_registration";
    private static final String EMAIL_COMP_ID = "companyRegistrationForm:email";
    private static final String DOMAIN_COMP_ID = "companyRegistrationForm:domain";
    private static final String FAILURE = "error_failure_short";
    private static final String REG_FAILURE = "error_registration_failure";
    private static final String ERROR_ADMIN_EMAIL = "error_admin_email";

    private CompanyForm companyRegistrationForm;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private UserServiceRemote userServiceRemote;
    @EJB
    private CompanyServiceRemote companyServiceRemote;

    @PostConstruct
    public final void init() {
        companyRegistrationForm = new CompanyForm();
    }

    public List<String> completeEmail(final String query) {
        List<String> emails = new ArrayList<>();
        for (UserVo userVo : userServiceRemote.findAll()) {
            if (userVo.getEmail().contains(query)) {
                emails.add(userVo.getEmail());
            }
        }
        return emails;
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (getAdminUserByEmail() == null) {
            context.addMessage(EMAIL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(ERROR_ADMIN_EMAIL)));
            return;
        }

        try {
            CompanyVo companyVo = companyRegistrationForm.getCompanyVo();
            companyVo.setAdminUser(getAdminUserByEmail());
            companyVo.getAdminUser().setCompanyName(companyVo.getCompanyName());
            companyVo.getAdminUser().setUserRole(UserRole.COMPANY_ADMIN);
            setDefaultValues(companyVo);

            companyVo = companyServiceRemote.save(companyVo);
            if (companyVo.getId() == null) {
                log.error("Failed to save company: {}", companyVo.getCompanyName());
                context.addMessage(DOMAIN_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        localeManagerBean.localize(FAILURE), localeManagerBean.localize(REG_FAILURE)));
            } else {
                log.info("Company '{}' successfully registered.", companyVo.getCompanyName());
                context.addMessage(DOMAIN_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        localeManagerBean.localize(SUCCESS), localeManagerBean.localize(SUCCESSFUL_REGISTRATION)));

                navigator.redirectTo(NavigatorBean.Pages.COMPANY_PROFILE, "id", companyVo.getId());
            }
        } catch (InvalidCompanyDataException e) {
            log.warn("Unsuccessful company registration attempt with data:{}{} ", System.getProperty("line.separator"), companyRegistrationForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), e);
        }
    }

    private void setDefaultValues(final CompanyVo companyVo) {
        companyVo.setAgents(new HashSet<>());
        companyVo.setQuotes(new QuotasVo());
        companyVo.setActive(true);
    }

    private UserVo getAdminUserByEmail() {
        return userServiceRemote.findByEmail(companyRegistrationForm.getAdminEmail());
    }

}
