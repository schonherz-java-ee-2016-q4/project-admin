package hu.schonherz.admin.web.locale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ManagedBean(name = "localeManagerBean")
@SessionScoped
public class LocaleManagerBean {

    private static final String PROPERTIES_FILE_NAME = "l10n.localization";

    private static final String SUCCESS = "success_short";
    private static final String FAILURE = "error_failure_short";

    private Locale locale;
    private ResourceBundle localeMessages;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        updateLocaleMessages();
    }

    public void setLanguage(final String language) {
        Locale facesLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        log.warn("facesLocale before: " + facesLocale.getLanguage());
        if (!locale.getLanguage().equals(language)) {
            locale = new Locale(language);
            updateLocaleMessages();
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void sendMessage(final String compId, final FacesMessage.Severity severity, final String detailedKey) {
        String localizedShort = severity == FacesMessage.SEVERITY_INFO ? localize(SUCCESS) : localize(FAILURE);
        String localizedDetailed = localize(detailedKey);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(compId, new FacesMessage(severity, localizedShort, localizedDetailed));
    }

    public String localize(final String key, final String... params) {
        if (params.length == 0) {
            return localeMessages.getString(key);
        }

        String pattern = getLocalizedPattern(key);
        return format(pattern, params);
    }

    private String getLocalizedPattern(final String key) {
        return localeMessages.getString(key);
    }

    private String format(final String pattern, final String... params) {
        MessageFormat formatter = new MessageFormat(pattern, getLocale());
        return formatter.format(params);
    }

    private void updateLocaleMessages() {
        localeMessages = ResourceBundle.getBundle(PROPERTIES_FILE_NAME, locale);
    }

}
