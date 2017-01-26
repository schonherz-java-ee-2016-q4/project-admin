package hu.schonherz.project.admin.web.view;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import hu.schonherz.project.admin.service.api.vo.TestUserVo;
import lombok.Data;

@ManagedBean(name = "registrationView")
@ViewScoped
@Data
public class RegistrationView {
    
    private TestUserVo testUserVo;

    @PostConstruct
    public void init() {
        testUserVo = new TestUserVo();
    }
    
    public void registration() {
//        Need implementation
    }
}
