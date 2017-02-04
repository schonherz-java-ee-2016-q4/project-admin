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
import hu.schonherz.project.admin.web.view.form.RegistrationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
@Slf4j
public class RegistrationView {

    // Short messages
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";
    // Detailed messages
    private static final String SUCCESSFUL_REGISTRATION = "Registration was successful";
    private static final String DUPLICATION = "Invalid user data, username or e-mail already in use";
    // Ids of message components
    private static final String BASE_COMP_ID = "registrationForm:";
    private static final String EMAIL_COMP_ID = BASE_COMP_ID + "email";
    private static final String USERNAME_COMP_ID = BASE_COMP_ID + "username";
    private static final String PASSWORD_COMP_ID = BASE_COMP_ID + "password";
    private static final String GLOBAL_COMP_ID = "registrationForm";

    // Wired to the registration xhtml
    private RegistrationForm form;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        form = new RegistrationForm();
        log.info("post construct");
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();

        log.info("in registration method");
        try {
            // Try to save user data
            UserVo userVo = form.getUserVo();
            userServiceRemote.registrationUser(userVo);
            log.info("saved it");

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS, SUCCESSFUL_REGISTRATION));
            log.info("User '{}' successfully registered.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE, DUPLICATION));
            log.warn("Unsuccessful registration attempt with data:{}{} ", System.getProperty("line.separator"), form);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

}
