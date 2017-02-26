package hu.schonherz.project.admin.web.view.security;

import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean;
import hu.schonherz.project.admin.web.view.navigation.NavigatorBean.Pages;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ManagedBean(name = "securityManagerBean")
@ApplicationScoped
public class SecurityManagerBean {

    @ManagedProperty(value = "#{navigatorBean}")
    private NavigatorBean navigator;

    @EJB
    private CompanyServiceRemote companyService;

    private Map<Pages, UserRole> permissionMap;
    // Buffer to store result of the isUserCompanyAdmin() method until the logged in user changes
    private Map<Long, Boolean> isCompanyAdmin;

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
        permissionMap.put(Pages.COMPANY_REPORT, UserRole.COMPANY_ADMIN);
        permissionMap.put(Pages.COMPANY_LIST, UserRole.ADMIN);

        isCompanyAdmin = new HashMap<>();
    }

    public boolean isPagePermitted(NavigatorBean.Pages page) {
        return isPagePermitted(page, true);
    }

    public boolean isPagePermitted(NavigatorBean.Pages page, boolean shouldRedirect) {
        UserVo user = getLoggedInUser();
        // Check if there is a logged in user
        if (user == null) {
            if (shouldRedirect) {
                log.warn("User tried to reach " + page.name() + " page without logging in.");
                navigator.redirectTo(Pages.LOGIN);
            }
            return false;
        }

        // Check if the user's permission is strong enough for the page
        UserRole minimumRole = permissionMap.get(page);
        if (minimumRole != null && user.getUserRole().getStrength() < minimumRole.getStrength()) {
            if (shouldRedirect) {
                log.warn("User " + user.getUsername() + " tried to reach " + page.name() + " without permission.");
                navigator.goToForbidden();
            }

            return false;
        }

        log.info("User " + user.getUsername() + " was permitted to visit " + page.name() + " page.");
        return true;
    }

    public boolean isUserCompanyAdmin() {
        UserVo loggedInUser = getLoggedInUser();
        if (loggedInUser == null) {
            return false;
        }
        Boolean isCompAdmin = isCompanyAdmin.get(loggedInUser.getId());
        if (isCompAdmin != null) {
            return isCompAdmin;
        }

        Long loggedInUserId = loggedInUser.getId();
        if (loggedInUser.getCompanyName() == null) {
            isCompanyAdmin.clear();
            isCompanyAdmin.put(loggedInUserId, false);
            return false;
        }

        if (loggedInUser.getUserRole() == UserRole.COMPANY_ADMIN) {
            isCompanyAdmin.clear();
            isCompanyAdmin.put(loggedInUserId, true);
            return true;
        }

        CompanyVo employerCompany = companyService.findByName(loggedInUser.getCompanyName());
        if (employerCompany == null) {
            isCompanyAdmin.clear();
            isCompanyAdmin.put(loggedInUserId, false);
            return false;
        }

        boolean result = Objects.equals(employerCompany.getAdminEmail(), loggedInUser.getEmail());
        isCompanyAdmin.clear();
        isCompanyAdmin.put(loggedInUserId, result);

        return result;
    }

    public UserVo getLoggedInUser() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return (UserVo) session.getAttribute("user");
    }

    public void updateLoggedInUser(@NonNull final UserVo user) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("user", user);
    }

}
