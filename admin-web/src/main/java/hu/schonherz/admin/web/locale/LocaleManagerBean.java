package hu.schonherz.admin.web.locale;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ManagedBean(name = "localeManagerBean")
@SessionScoped
public class LocaleManagerBean {

    private static final String PROPERTIES_FILE_NAME = "l10n.localization";

    private Locale locale;
    private ResourceBundle localeMessages;

    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
        updateLocaleMessages();
    }

    public void setLanguage(final String language) {
        if (!locale.getLanguage().equals(language)) {
            locale = new Locale(language);
            updateLocaleMessages();
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }
    }

    private void updateLocaleMessages() {
        localeMessages = ResourceBundle.getBundle(PROPERTIES_FILE_NAME, locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getLocaleMessages() {
        return localeMessages;
    }

}
