package hu.schonherz.project.admin.web.view;

import java.util.ArrayList;
import java.util.List;

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

@ManagedBean(name = "usersView")
@ViewScoped
@Data
public class UsersView {

    private List<UserVo> users;

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

    /*
     * This method is here only to convince the pmd that the 'users' list should
     * be a field not a local variable.
     */
//    public void temp() {
//        if (users == null) {
//            users = new ArrayList<>();
//        }
//    }
    public void deleteUser(@NonNull final UserVo userVo) {
        userServiceRemote.delete(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Done!", userVo.getUsername() + " is deleted."));
    }

    public void changeUserStatus(@NonNull final UserVo userVo) {
        userServiceRemote.changeStatus(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Done!",
                userVo.getUsername() + " is " + (!userVo.isActive() ? "active" : "inactive") + " now"));
    }

    public void resetUserPassword(@NonNull final UserVo userVo) {
        userServiceRemote.resetPassword(userVo.getId());
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null,
                new FacesMessage("Done!", "Password is default now. Email was sent to " + userVo.getUsername()));
    }

}
