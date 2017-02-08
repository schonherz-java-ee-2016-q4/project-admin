package hu.schonherz.project.admin.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "usersView")
@ViewScoped
@Data
@Slf4j
public class UsersView {

    private static final String CHANGING_SUCCESS = "growl_done";
    private static final String DELETE_SUCCESS = "growl_delete_success";
    private static final String ACTIVATE_SUCCESS = "growl_activate_success";
    private static final String INACTIVATE_SUCCESS = "growl_inactivate_success";
    private static final String RESET_PASSWORD_SUCCESS = "growl_reset_password_success";
    private static final String SPACE = " ";
    private List<UserVo> users;
    private ResourceBundle localMessages;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        initializeList();
        users = userServiceRemote.findAll();
        try {
            localMessages = ResourceBundle.getBundle("i18n.localization");
        } catch (Exception e) {
            String message = "Could not create resource bundle for localization messages!";
            log.error(message, e);
            throw new IllegalStateException(message, e);
        }
    }

    private void initializeList() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    /*
     * This method is here only to convince the pmd that the 'users' list should
     * be a field not a local variable.
     */
    // public void temp() {
    // if (users == null) {
    // users = new ArrayList<>();
    // }
    // }
    public void deleteUser(@NonNull final UserVo userVo) {
        userServiceRemote.delete(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(localMessages.getString(CHANGING_SUCCESS),
                localMessages.getString(DELETE_SUCCESS) + SPACE + userVo.getUsername()));
    }

    public void changeUserStatus(@NonNull final UserVo userVo) {
        userServiceRemote.changeStatus(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage(localMessages.getString(CHANGING_SUCCESS),
                        (userVo.isActive() ? localMessages.getString(INACTIVATE_SUCCESS)
                                : localMessages.getString(ACTIVATE_SUCCESS)) + SPACE + userVo.getUsername()));
    }

    public void resetUserPassword(@NonNull final UserVo userVo) {
        userServiceRemote.resetPassword(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(localMessages.getString(CHANGING_SUCCESS),
                localMessages.getString(RESET_PASSWORD_SUCCESS) + SPACE + userVo.getUsername()));
    }

}
