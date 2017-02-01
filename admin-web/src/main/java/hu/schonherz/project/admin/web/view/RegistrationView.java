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
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
public class RegistrationView {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationView.class);

    private UserVo userVo;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        userVo = new UserVo();
    }

    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Basic validation
        if (!isUserDataValid()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed", "All fields must be filled, password must be at least 6 character long."));
            LOG.info("User tried to register without filling all fields.");
            return;
        }

        // Try to save user data. Notify the user about the result.
        try {
            userServiceRemote.registrationUser(userVo);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Registration was successful."
                    + "Username or email already in use."));
            LOG.info("User '{}' successfully registered.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed!", "Invalid user data. "
                    + "Username or email already in use."));
            LOG.warn("Unsuccessful registration attempt with data:{}{} ",
                    System.getProperty("line.separator"), userVo);
            LOG.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

    private boolean isUserDataValid() {
        if (hasEmptyUserData()) {
            return false;
        }

        final int minPasswordLength = 6;
        return userVo.getPassword().length() >= minPasswordLength;
    }

    private boolean hasEmptyUserData() {
        return !(userVo.getEmail() != null && !userVo.getEmail().isEmpty()
                && userVo.getUsername() != null && !userVo.getUsername().isEmpty()
                && userVo.getPassword() != null && !userVo.getPassword().isEmpty());
    }
}
