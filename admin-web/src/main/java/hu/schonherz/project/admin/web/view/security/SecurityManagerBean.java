package hu.schonherz.project.admin.web.view.security;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static hu.schonherz.project.admin.web.view.navigation.NavigatorBean.Pages;

@Data
@Slf4j
@ManagedBean(name = "securityManagerBean")
@ApplicationScoped
public class SecurityManagerBean {

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;
    private Map<Pages, UserRole> permissionMap;

    @PostConstruct
    public void init() {
        permissionMap = new HashMap<>();
        permissionMap.put(Pages.LOGIN, null);
        permissionMap.put(Pages.USER_REGISTRATION, null);
        permissionMap.put(Pages.ERROR_PAGE, null);

        permissionMap.put(Pages.USER_PROFILE, UserRole.AGENT);
        permissionMap.put(Pages.USER_LIST, UserRole.COMPANY_ADMIN);

        permissionMap.put(Pages.COMPANY_REGISTRATION, UserRole.ADMIN);
        permissionMap.put(Pages.COMPANY_PROFILE, UserRole.COMPANY_ADMIN);
        permissionMap.put(Pages.COMPANY_LIST, UserRole.ADMIN);
    }

    public boolean isPagePermitted(NavigatorBean.Pages page) {
        FacesContext context = FacesContext.getCurrentInstance();
        UserVo user = getLoggedInUser(context);
        // Check if there is a logged in user
        if (user == null) {
            log.warn("User tried to reach " + page.name() + " page without logging in.");
            navigator.redirectTo(Pages.LOGIN);
            return false;
        }

        // Check if the user's permission is strong enough for the page
        UserRole minimumRole = permissionMap.get(page);
        if (minimumRole != null && user.getUserRole().getStrength() < minimumRole.getStrength()) {
            log.warn("User " + user.getUsername() + " tried to reach " + page.name() + " without permission.");
            navigator.redirectTo(Pages.ERROR_PAGE);
            return false;
        }

        log.info("User " + user.getUsername() + " was permitted to visit " + page.name() + " page.");
        return true;
    }

    private UserVo getLoggedInUser(FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }

}