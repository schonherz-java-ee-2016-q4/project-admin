package hu.schonherz.project.admin.web.validator;


import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
@FacesValidator("adminEmailValidator")
public class AdminEmailValidator implements Validator {

    private static final String FAILURE = "error_failure_short";
    private static final String ERROR_ADMIN_EMAIL = "error_admin_email";
    private static final String ERROR_INVALID_EMAIL = "error_invalid_email";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final Pattern pattern;

    @EJB
    private UserServiceRemote userServiceRemote;

    public AdminEmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Matcher matcher;
        matcher = pattern.matcher(o.toString());
        if (!matcher.matches()) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FAILURE", "Email is not valid.");
            throw new ValidatorException(facesMessage);
        } else if (!getEmailList().contains(o.toString())) {
            FacesMessage facesMessage = new FacesMessage("There's no user with this email.");
            throw new ValidatorException(facesMessage);
        }
    }

    private List<String> getEmailList() {
        List<String> emails = new ArrayList<>();
        for (UserVo userVo : userServiceRemote.findAll()) {
            emails.add(userVo.getEmail());
        }
        return emails;
    }

}
