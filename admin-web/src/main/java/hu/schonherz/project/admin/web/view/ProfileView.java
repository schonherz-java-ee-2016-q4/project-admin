package hu.schonherz.project.admin.web.view;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.service.exception.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;

@ManagedBean(name = "profileView")
@ViewScoped
@Data
public class ProfileView {
    
    private UserVo userVo;
    
    @EJB
    private UserServiceRemote userServiceRemote;
    
    @PostConstruct
    public void buildUser() {
//        Mock username
        final String username = "redsnake327";
        userVo = userServiceRemote.findByUsername(username);
    }
    
    public void save() {
//        FacesContext context = FacesContext.getCurrentInstance();
        try {
            userServiceRemote.registrationUser(userVo);
        } catch (InvalidUserDataException e) {
          
        }
    }
}
