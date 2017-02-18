package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.LoginForm;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "loginView")
@ViewScoped
@Data
@Slf4j
public class LoginView {

    private static final String MESSAGE_COMP_ID = "loginForm:password";
    private static final String FAILURE = "error_failure_short";
    private static final String FAILED_LOGIN = "growl_invalid_username_or_password";

    private LoginForm loginForm;

    @EJB
    private UserServiceRemote userService;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManager;
    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @PostConstruct
    public void init() {
        loginForm = new LoginForm();
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();

        UserVo user = userService.findByUsername(loginForm.getUsername());
        if (user == null) {
            sendErrorMessage(context);
            init();
            return;
        }

        if (!Encrypter.match(user.getPassword(), loginForm.getPassword())) {
            sendErrorMessage(context);
            init();
            return;
        }

        HttpSession session = getSession();
        session.setAttribute("user", user);

        navigator.redirectTo(NavigatorBean.Pages.USER_PROFILE);
    }

    public void logout() {
        HttpSession session = getSession();
        session.removeAttribute("user");
        navigator.redirectTo(NavigatorBean.Pages.LOGIN);
    }

    private void sendErrorMessage(final FacesContext context) {
        context.addMessage(MESSAGE_COMP_ID, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                localeManager.localize(FAILURE), localeManager.localize(FAILED_LOGIN)));
    }

    private HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpSession) context.getExternalContext().getSession(true);
    }

}
