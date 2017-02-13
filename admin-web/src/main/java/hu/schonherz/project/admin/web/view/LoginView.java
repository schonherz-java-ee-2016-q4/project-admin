package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.form.LoginForm;
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

    private static final String SUCCESS = "success_short";
    private static final String FAILURE = "error_failure_short";
    private static final String SUCCESSFUL_LOGIN = "success_login";
    private static final String FAILED_LOGIN = "error_invalid_username_or_password";

    private LoginForm loginForm;

    @EJB
    private UserServiceRemote userService;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManager;

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

        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("user", user);

        sendSuccessMessage(context);
    }

    private void sendSuccessMessage(final FacesContext context) {
        sendMessage(context, FacesMessage.SEVERITY_INFO, SUCCESS, SUCCESSFUL_LOGIN);
    }

    private void sendErrorMessage(final FacesContext context) {
        sendMessage(context, FacesMessage.SEVERITY_ERROR, FAILURE, FAILED_LOGIN);
    }

    private void sendMessage(final FacesContext context, final FacesMessage.Severity severity, final String summaryKey, final String detailedKey) {
//        String messageCompId = "loginForm";
        context.addMessage(null, new FacesMessage(severity,
                localeManager.localize(summaryKey), localeManager.localize(detailedKey)));

    }

}
