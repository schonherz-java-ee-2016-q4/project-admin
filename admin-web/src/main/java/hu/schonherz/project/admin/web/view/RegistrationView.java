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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
public class RegistrationView {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationView.class);

    private RegistrationForm form;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        form = new RegistrationForm();
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();
        final String componentId = "registratonForm";
        // Basic validation
        if (hasEmptyUserData()) {
            context.addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", "All fields must be filled"));
            LOG.warn("User tried to register without filling all fields.");
            return;
        }

        // Password length validation
        final int minPasswordLength = 6;
        if (form.getPassword().length() < minPasswordLength) {
            context.addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", "Password must be at least " + minPasswordLength + " character long."));
            LOG.warn("User tried to register with a short password.");
            return;
        }

        // Confirm password validation
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            context.addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", "Password and Confirm password fields must be equal."));
            LOG.warn("User failed to confirm his password.");
            return;
        }

        // Try to save user data. Notify the user about the result.
        try {
            UserVo userVo = form.getUserVo();
            userServiceRemote.registrationUser(userVo);
            context.addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Registration was successful."));
            LOG.info("User '{}' successfully registered.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            context.addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "Invalid user data. "
                    + "Username or email already in use."));
            LOG.warn("Unsuccessful registration attempt with data:{}{} ",
                    System.getProperty("line.separator"), form);
            LOG.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

    private boolean hasEmptyUserData() {
        return !(form.getEmail() != null && !form.getEmail().isEmpty()
                && form.getUsername() != null && !form.getUsername().isEmpty()
                && form.getPassword() != null && !form.getPassword().isEmpty()
                && form.getConfirmPassword() != null && !form.getConfirmPassword().isEmpty());
    }
}
