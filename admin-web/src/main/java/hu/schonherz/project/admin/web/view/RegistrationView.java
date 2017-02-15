package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.user.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.RegistrationForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
@Slf4j
public class RegistrationView {

    // Short messages
    private static final String SUCCESS = "success_short";
    private static final String FAILURE = "error_failure_short";
    // Detailed messages
    private static final String SUCCESSFUL_REGISTRATION = "success_registration";
    private static final String DUPLICATION = "error_duplication";
    // Ids of message components
    private static final String GLOBAL_COMP_ID = "registrationForm";

    // Wired to the registration xhtml
    private RegistrationForm form;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        form = new RegistrationForm();
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            UserVo userVo = form.getUserVo();
            setDefaultValues(userVo);
            log.warn("--------- userVo: " + userVo.toString());

            // Try to save user data
            userVo.setPassword(Encrypter.encrypt(form.getUserVo().getPassword()));
            log.warn("--------- userVo: " + userVo.toString());
            // This vo has ID
            userVo = userServiceRemote.registrationUser(userVo);
            log.warn("--------- userVo: " + userVo.toString());

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localeManagerBean.localize(SUCCESS), localeManagerBean.localize(SUCCESSFUL_REGISTRATION)));

            log.info("User '{}' successfully registered.", userVo.getUsername());

            // Automatically login user and redirect to profile page
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("user", userVo);
            navigator.redirectTo(NavigatorBean.Pages.USER_PROFILE);
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(DUPLICATION)));

            log.warn("Unsuccessful registration attempt with data:{}{} ", System.getProperty("line.separator"), form);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

    private void setDefaultValues(final UserVo vo) {
        vo.setActive(true);
        vo.setUserRole(UserRole.AGENT);
    }

}
