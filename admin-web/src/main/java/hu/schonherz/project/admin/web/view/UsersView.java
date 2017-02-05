package hu.schonherz.project.admin.web.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;

@ManagedBean(name = "usersView")
@ViewScoped
@Data
public class UsersView {

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
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

    /* This method is here only to convince the pmd that the 'users' list should be a field not a local variable. */
    public void temp() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public void deleteUser(UserVo userVo) {
        userServiceRemote.delete(userVo.getId());
    }

    public void changeUserStatus(UserVo userVo) {
        userServiceRemote.changeStatus(userVo.getId());
        init();
    }

    public void resetUserPassword(UserVo userVo) {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(8);
        encoder.encode(generatedPassword);
        //Need implementation
    }


}
