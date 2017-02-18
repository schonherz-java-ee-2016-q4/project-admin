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
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.ProfileForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import hu.schonherz.project.admin.web.view.security.SecurityManagerBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "profileView")
@ViewScoped
@Data
@Slf4j
public class ProfileView {

    private static final String USER_PARAM_NAME = "user";
    // Short messages
    private static final String SUCCESS = "success_short";
    private static final String FAILURE = "error_failure_short";
    // Detailed messages
    private static final String SUCCESSFUL_CHANGING = "success_profile_changes";
    private static final String DUPLICATION_EMAIL = "error_duplication_email";
    private static final String INVALID_PASSWORD = "error_invalid_password";
    // Ids of message components
    private static final String PASSWORD_COMP_ID = "profileForm:currentPassword";
    private static final String MESSAGE_COMP_ID = "profileForm:newPasswordConfirm";

    // Wired to the profile xhtml
    private ProfileForm profileForm;
    private boolean disableNewPassword;
    // Data of the currently edited user
    private UserVo currentUserVo;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @ManagedProperty(value = "#{securityManagerBean}")
    private SecurityManagerBean securityManagerBean;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        // Get user whose profile we display
        FacesContext context = FacesContext.getCurrentInstance();
        String userIdParameter = context.getExternalContext().getRequestParameterMap().get("id");
        if (userIdParameter == null) {
            // If there is no id parameter, than the logged in user's profile it is
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            currentUserVo = (UserVo) session.getAttribute(USER_PARAM_NAME);
            log.info("User " + currentUserVo.getUsername() + " is visiting his own profile.");
        } else {
            // If there is an id parameter, get that user from the database
            currentUserVo = userServiceRemote.findById(Long.valueOf(userIdParameter));
            log.info("Somebody with high role is visiting profile of user " + currentUserVo.getUsername() + ".");
        }

        profileForm = new ProfileForm(currentUserVo);
        disableNewPassword = true;
    }

    public void save() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Password should match the one read from the database
        if (!Encrypter.match(currentUserVo.getPassword(), profileForm.getPassword())) {
            context.addMessage(PASSWORD_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(INVALID_PASSWORD)));
            return;
        }

        try {
            currentUserVo = prepareUserToSave();
            userServiceRemote.registrationUser(currentUserVo);

            // Notify user about success and log it
            context.addMessage(MESSAGE_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localeManagerBean.localize(SUCCESS), localeManagerBean.localize(SUCCESSFUL_CHANGING)));

            log.info("User '{}' successfully changed his/her profile.", currentUserVo.getUsername());
            updateSessionOrRedirect();
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(MESSAGE_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(DUPLICATION_EMAIL)));

            log.warn("Unsuccessful changing attempt with data:{}{} ", System.getProperty("line.separator"), profileForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

    private void updateSessionOrRedirect() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(true);
        Long sessionUserId = ((UserVo) session.getAttribute(USER_PARAM_NAME)).getId();
        Long modifiedUserId = currentUserVo.getId();

        // Don't redirect if user is on his own profile page
        if (sessionUserId.equals(modifiedUserId)) {
            session.setAttribute(USER_PARAM_NAME, currentUserVo);
        } else {
            navigator.redirectTo(NavigatorBean.Pages.USER_LIST);
        }
    }

    private UserVo prepareUserToSave() {
        UserVo userVo = profileForm.getUserVo();
        userVo.setPassword(currentUserVo.getPassword());
        userVo.setActive(currentUserVo.isActive());
        String newPassword = profileForm.getNewPassword();
        if (!disableNewPassword) {
            userVo.setPassword(Encrypter.encrypt(newPassword));
        }

        return userVo;
    }

}
