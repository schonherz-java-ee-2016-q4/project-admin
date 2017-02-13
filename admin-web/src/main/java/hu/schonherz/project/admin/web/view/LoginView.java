package hu.schonherz.project.admin.web.view;

import hu.schonherz.project.admin.web.view.form.LoginForm;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "loginView")
@ViewScoped
@Data
@Slf4j
public class LoginView {

    private LoginForm loginForm;

    @PostConstruct
    public void init() {
        loginForm = new LoginForm();
    }

    public void login() {
        if (loginForm != null) {
            log.info("------------- ment -------------");
        }
        // TODO
    }

}
