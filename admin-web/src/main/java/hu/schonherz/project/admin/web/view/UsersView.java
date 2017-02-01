package hu.schonherz.project.admin.web.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;

@ManagedBean(name = "usersView")
@ViewScoped
@Data
public class UsersView {

    private List<UserVo> users;

    @EJB
    private UserServiceRemote userServiceRemote;

    @PostConstruct
    public void init() {
        users = userServiceRemote.findAll();
    }

}
