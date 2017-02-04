package hu.schonherz.project.admin.web.view;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.exception.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.FormValidator;
import hu.schonherz.project.admin.web.view.form.FormValidator.MessageBinding;
import hu.schonherz.project.admin.web.view.form.ProfileForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "profileView")
@ViewScoped
@Data
@Slf4j
public class ProfileView {

    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";
    private static final String SUCCESSFUL_CHANGING = "Your changes were successfully saved.";
    private static final String DUPLICATION_EMAIL = "Invalid email data, because it's already in use!";
    private static final String BASE_COMP_ID = "profileForm:";
    private static final String EMAIL_COMP_ID = BASE_COMP_ID + "email";
    private static final String PASSWORD_COMP_ID = BASE_COMP_ID + "currentPassword";
    private static final String NEWPASSWORD_COMP_ID = BASE_COMP_ID + "newPassword";
    private static final String GLOBAL_COMP_ID = "profileForm";
    
    private ProfileForm profileForm;
    
    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        profileForm = new ProfileForm();
        UserVo userVo = userServiceRemote.findAll().get(0);
        profileForm.setEmail(userVo.getEmail());
        profileForm.setUsername(userVo.getUsername());
        log.info(userVo.toString());
        log.info(profileForm.toString());
    }

    public void save() {
        FacesContext context = FacesContext.getCurrentInstance();
        MessageBinding messageBinding = FormValidator.validateProfileForm(profileForm);
        if (!messageBinding.isEmpty()) {
            sendValidationMessages(messageBinding, context);
            return;
        }
        
        try {
            // Try to save user data
            UserVo userVo = profileForm.getUserVo();
            userServiceRemote.registrationUser(userVo);

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS, SUCCESSFUL_CHANGING));
            log.info("User '{}' successfully changed his/her profile.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE, DUPLICATION_EMAIL));
            log.warn("Unsuccessful changing attempt with data:{}{} ", System.getProperty("line.separator"), profileForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }
    
    private void sendValidationMessages(MessageBinding messageBinding, FacesContext context) {
        String message;
        // Send e-mail validation message if there is one
        if (messageBinding.hasMessageOfType(MessageBinding.MessageType.EMAIL)) {
            message = messageBinding.getMessage(MessageBinding.MessageType.EMAIL);
            context.addMessage(EMAIL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE, message));
        }
        
        // Send password validation message if there is one
        if (messageBinding.hasMessageOfType(MessageBinding.MessageType.PASSWORD)) {
            message = messageBinding.getMessage(MessageBinding.MessageType.PASSWORD);
            context.addMessage(PASSWORD_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE, message));
        }
        //Send new password validation message if there is one
        if (messageBinding.hasMessageOfType(MessageBinding.MessageType.NEW_PASSWORD)) {
            message = messageBinding.getMessage(MessageBinding.MessageType.NEW_PASSWORD);
            context.addMessage(NEWPASSWORD_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE, message));
        }
        
    }
}
