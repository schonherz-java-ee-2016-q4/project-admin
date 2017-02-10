package hu.schonherz.admin.web.locale;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ManagedBean
@SessionScoped
public class LocaleManagerBean {

    public Locale getLocale() {
        return LocalizationManagement.getLocale();
    }

    public String getLanguage() {
        return LocalizationManagement.getLanguage();
    }

    public void setLanguage(final String language) {
        if (LocalizationManagement.setLanguage(language)) {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(LocalizationManagement.getLocale());
        }
    }

}
