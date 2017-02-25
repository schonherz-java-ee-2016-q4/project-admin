package hu.schonherz.project.admin.web.view.security;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "errorBean")
@ViewScoped
public class ErrorBean {

    private static final String STATUS_PARAMETER_NAME = "statusCode";
    private static final String FORBIDDEN_CODE = "403";
    private static final String FORBIDDEN_MESSAGE = "";

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManagerBean;

    private String statusCode;
    private String message;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        statusCode = context.getExternalContext().getRequestParameterMap().get(STATUS_PARAMETER_NAME);
        if (FORBIDDEN_CODE.equals(statusCode)) {
            message = localeManagerBean.localize(FORBIDDEN_MESSAGE);
        }
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocaleManagerBean getLocaleManagerBean() {
        return localeManagerBean;
    }

    public void setLocaleManagerBean(LocaleManagerBean localeManagerBean) {
        this.localeManagerBean = localeManagerBean;
    }

}
