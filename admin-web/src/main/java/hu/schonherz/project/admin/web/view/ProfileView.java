package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.ProfileForm;
import javax.faces.bean.ManagedProperty;
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
    private static final String PASSWORD_COMP_ID = "profileForm:currentPassword";
    private static final String GLOBAL_COMP_ID = "profileForm";

    // Wired to the profile xhtml
    private ProfileForm profileForm;
    private boolean disableNewPassword;
    // Data of the currently edited user
    private UserVo currentUserVo;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        Long userId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
        log.info("UserId value: {}", userId);
        currentUserVo = userServiceRemote.findById(userId);
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
            // Get user vo and set new password if given
            UserVo userVo = profileForm.getUserVo();
            userVo.setPassword(currentUserVo.getPassword());
            userVo.setActive(currentUserVo.isActive());
            String newPassword = profileForm.getNewPassword();
            if (!disableNewPassword) {
                userVo.setPassword(Encrypter.encrypt(newPassword));
            }

            userServiceRemote.registrationUser(userVo);
            currentUserVo = userVo;

            // Notify user about success and log it
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    localeManagerBean.localize(SUCCESS), localeManagerBean.localize(SUCCESSFUL_CHANGING)));

            log.info("User '{}' successfully changed his/her profile.", userVo.getUsername());
        } catch (InvalidUserDataException iude) {
            // Notify user about duplication and log it with details
            context.addMessage(GLOBAL_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    localeManagerBean.localize(FAILURE), localeManagerBean.localize(DUPLICATION_EMAIL)));

            log.warn("Unsuccessful changing attempt with data:{}{} ", System.getProperty("line.separator"), profileForm);
            log.warn("Causing exception:" + System.getProperty("line.separator"), iude);
        }
    }

}
