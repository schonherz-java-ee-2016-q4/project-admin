package hu.schonherz.project.admin.web.view;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.exception.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.ProfileForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "profileView")
@ViewScoped
@Data
@Slf4j
public class ProfileView {

    // Short messages
    private static final String SUCCESS = "success_short";
    private static final String FAILURE = "error_failure_short";
    // Detailed messages
    private static final String SUCCESSFUL_CHANGING = "success_profile_changes";
    private static final String DUPLICATION_EMAIL = "error_duplication_email";
    private static final String INVALID_PASSWORD = "error_invalid_password";
    // Ids of message components
    private static final String BASE_COMP_ID = "profileForm:";
    private static final String PASSWORD_COMP_ID = BASE_COMP_ID + "currentPassword";
    private static final String GLOBAL_COMP_ID = "profileForm";

    // Wired to the profile xhtml
    private ProfileForm profileForm;
    private boolean disableNewPassword;
    // Data of the currently edited user
    private UserVo currentUserVo;
    private ResourceBundle localMessages;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        Long userId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        log.info("UserId value: {}", userId);
        currentUserVo = userServiceRemote.findById(userId);
        profileForm = new ProfileForm(currentUserVo);
        disableNewPassword=true;
        try {
            localMessages = ResourceBundle.getBundle("i18n.localization");
        } catch (Exception e) {
            String message = "Could not create resource bundle for localization messages!";
            log.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    public void save() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Password should match the one read from the database
        if (!Encrypter.match(currentUserVo.getPassword(), profileForm.getPassword())) {
            context.addMessage(PASSWORD_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localMessages.getString(FAILURE), localMessages.getString(INVALID_PASSWORD)));
            return;
        }

        try {
            // Get user vo and set new password if given
            UserVo userVo = profileForm.getUserVo();
            userVo.setPassword(currentUserVo.getPassword());
            String newPassword = profileForm.getNewPassword();
            if (!disableNewPassword) {
                userVo.setPassword(Encrypter.encrypt(newPassword));
            }

            userServiceRemote.registrationUser(userVo);
            currentUserVo = userVo;

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localMessages.getString(SUCCESS), localMessages.getString(SUCCESSFUL_CHANGING)));

            log.info("User '{}' successfully changed his/her profile.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localMessages.getString(FAILURE), localMessages.getString(DUPLICATION_EMAIL)));

            log.warn("Unsuccessful changing attempt with data:{}{} ", System.getProperty("line.separator"), profileForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

}
