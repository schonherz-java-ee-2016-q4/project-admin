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
import hu.schonherz.project.admin.web.view.form.RegistrationForm;
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
    private ResourceBundle localMessages;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        form = new RegistrationForm();
        try {
            localMessages = ResourceBundle.getBundle("i18n.localization");
        } catch (Exception e) {
            String message = "Could not create resource bundle for localization messages!";
            log.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Try to save user data
            UserVo userVo = form.getUserVo();
            userVo.setPassword(Encrypter.encrypt(form.getUserVo().getPassword()));
            userServiceRemote.registrationUser(userVo);

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localMessages.getString(SUCCESS), localMessages.getString(SUCCESSFUL_REGISTRATION)));

            log.info("User '{}' successfully registered.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localMessages.getString(FAILURE), localMessages.getString(DUPLICATION)));

            log.warn("Unsuccessful registration attempt with data:{}{} ", System.getProperty("line.separator"), form);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

}
