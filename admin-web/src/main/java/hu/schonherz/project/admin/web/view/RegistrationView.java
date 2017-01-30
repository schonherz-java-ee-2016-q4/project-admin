package hu.schonherz.project.admin.web.view;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.Data;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
public class RegistrationView {
    
    private UserVo testUserVo;

    @EJB
    private UserServiceRemote userServiceRemote;
    
    @PostConstruct
    public void init() {
        testUserVo = new UserVo();
    }
    
    public void registration() {
        FacesContext context = FacesContext.getCurrentInstance();
        userServiceRemote.registrationUser(testUserVo);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Registration!"));
    }
}
