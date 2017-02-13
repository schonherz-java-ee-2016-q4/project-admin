package hu.schonherz.project.admin.web.view.navigation;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ManagedBean(name = "navigatorBean")
@ApplicationScoped
public class NavigatorBean {

    private static final String PUBLIC = "/pages/public/";
    private static final String SECURED_USER = "/pages/secured/";
    private static final String SECURED_COMPANY = "/pages/secured/company/";

    public enum Pages {
        LOGIN(PUBLIC + "login.xhtml"),
        USER_REGISTRATION(PUBLIC + "registration.xhtml"),
        USER_PROFILE(SECURED_USER + "profile.xhtml"),
        USER_LIST(SECURED_USER + "users.xhtml"),
        COMPANY_REGISTRATION(SECURED_COMPANY + "registration.xhtml"),
        COMPANY_PROFILE(SECURED_COMPANY + "profile.xhtml"),
        COMPANY_LIST(SECURED_COMPANY + "companies.xhtml");

        private final String url;

        Pages(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }

    public void redirectTo(@NonNull final FacesContext context, @NonNull final Pages toPage, final String... params) {
        String fullUrl = createFullUrl(toPage.getUrl(), params);
        context.getApplication().getNavigationHandler().handleNavigation(context, null, fullUrl);
    }

    private String createFullUrl(final String url, final String... params) {
        String fullUrl = url;

        if (params != null) {
            for (String param : params) {
                fullUrl = addParameter(fullUrl, param);
            }
        }

        return enableRedirect(fullUrl);
    }

    private String enableRedirect(final String url) {
        return addParameter(url, "faces-redirect=true");
    }

    private String addParameter(final String url, final String parameter) {
        char separator = url.contains("?") ? '&' : '?';
        return url + separator + parameter;
    }

}
