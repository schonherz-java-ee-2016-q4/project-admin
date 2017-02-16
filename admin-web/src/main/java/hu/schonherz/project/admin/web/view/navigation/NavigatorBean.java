package hu.schonherz.project.admin.web.view.navigation;

import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import lombok.NonNull;

@ManagedBean(name = "navigatorBean")
@ApplicationScoped
public class NavigatorBean {

    // Every parameter that we put in the request have this prefix, so we can separate our own parameters from the others.
    public static final String PARAM_PREFIX = "adminParam:";

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
        COMPANY_LIST(SECURED_COMPANY + "companies.xhtml"),
        ERROR_PAGE("/dummyErrorPage.xhtml");

        private final String url;

        Pages(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }

    public void redirectTo(@NonNull final Pages toPage, final String paramKey, final Object paramValue) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(paramKey, paramValue.toString());
        redirectTo(toPage, paramMap);
    }

    public void redirectTo(@NonNull final Pages toPage, final Map<String, String> params) {
        String fullUrl = createFullUrl(toPage.getUrl(), params);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getApplication().getNavigationHandler().handleNavigation(context, null, fullUrl);
    }

    public void redirectTo(@NonNull final Pages toPage) {
        redirectTo(toPage, null);
    }

    private String createFullUrl(final String url, final Map<String, String> params) {
        String fullUrl = url;

        if (params != null) {
            for (Map.Entry<String, String> paramEntry : params.entrySet()) {
                fullUrl = addParameter(fullUrl, paramEntry.getKey(), paramEntry.getValue());
            }
        }

        return enableRedirect(fullUrl);
    }

    private String enableRedirect(final String url) {
        return addParameter(url, "faces-redirect", "true");
    }

    private String addParameter(final String url, final String paramKey, final String paramValue) {
        char separator = url.contains("?") ? '&' : '?';
        return url + separator + paramKey + '=' + paramValue;
    }

}