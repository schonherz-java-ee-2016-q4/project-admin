package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;

import javax.faces.bean.ManagedProperty;

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

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        initializeList();
        users = userServiceRemote.findAll();
    }

    private void initializeList() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public void deleteUser(@NonNull final UserVo userVo) {
        userServiceRemote.delete(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(localeManagerBean.localize(CHANGING_SUCCESS),
                localeManagerBean.localize(DELETE_SUCCESS, userVo.getUsername())));
    }

    public void changeUserStatus(@NonNull final UserVo userVo) {
        userServiceRemote.changeStatus(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage(localeManagerBean.localize(CHANGING_SUCCESS),
                        (userVo.isActive() ? localeManagerBean.localize(INACTIVATE_SUCCESS)
                        : localeManagerBean.localize(ACTIVATE_SUCCESS)) + SPACE + userVo.getUsername()));
    }

    public void resetUserPassword(@NonNull final UserVo userVo) {
        userServiceRemote.resetPassword(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(localeManagerBean.localize(CHANGING_SUCCESS),
                localeManagerBean.localize(RESET_PASSWORD_SUCCESS) + SPACE + userVo.getUsername()));
    }

}
