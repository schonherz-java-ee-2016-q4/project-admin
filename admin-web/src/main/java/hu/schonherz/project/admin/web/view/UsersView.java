package hu.schonherz.project.admin.web.view;

import hu.schonherz.admin.web.locale.LocaleManagerBean;
import hu.schonherz.project.admin.service.api.service.company.CompanyServiceRemote;
import hu.schonherz.project.admin.service.api.service.company.InvalidCompanyDataException;
import hu.schonherz.project.admin.service.api.service.user.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.CompanyVo;
import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.web.view.security.SecurityManagerBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@ManagedBean(name = "usersView")
@ViewScoped
@Data
@Slf4j
public class UsersView {

    private static final String FAILURE = "error_failure_short";
    private static final String DELETE_COMPANY_ADMIN = "error_delete_company_admin";
    private static final String DELETE_ADMIN = "error_delete_admin";
    private static final String CHANGING_SUCCESS = "growl_done";
    private static final String DELETE_SUCCESS = "growl_delete_success";
    private static final String ACTIVATE_SUCCESS = "growl_activate_success";
    private static final String INACTIVATE_SUCCESS = "growl_inactivate_success";
    private static final String RESET_PASSWORD_SUCCESS = "growl_reset_password_success";
    private List<UserVo> users;

    @ManagedProperty(value = "#{localeManagerBean}")
    private LocaleManagerBean localeManager;

    @ManagedProperty(value = "#{securityManagerBean}")
    private SecurityManagerBean securityManagerBean;

    @EJB
    private UserServiceRemote userServiceRemote;

    @EJB
    private CompanyServiceRemote companyServiceRemote;

    @PostConstruct
    public void init() {
        initializeList();
        UserVo loggedInUser = securityManagerBean.getLoggedInUser();
        if (loggedInUser.getUserRole() == UserRole.ADMIN) {
            users = userServiceRemote.findAll();
        } else { // only COMPANY_ADMIN can be the other, because AGENT -s are not permitted here
            users = new ArrayList<>(userServiceRemote.findByCompanyName(loggedInUser.getCompanyName()));
        }
    }

    private void initializeList() {
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    public void deleteUser(@NonNull final UserVo userVo) {
        if (userVo.getUserRole() == UserRole.ADMIN) {
            log.warn("Tried to delete admin user: {}", userVo.getUsername());
            sendMessage(localeManager.localize(FAILURE), localeManager.localize(DELETE_ADMIN, userVo.getUsername()));
            return;
        }

        String employerCompanyName = userVo.getCompanyName();
        if (employerCompanyName != null) {
            // Not allowed to directly delete company admin
            if (userVo.getUserRole() == UserRole.COMPANY_ADMIN) {
                log.warn("Tried to delete company admin of {}", employerCompanyName);
                String shortMessage = localeManager.localize(FAILURE);
                String detailedMessage = localeManager.localize(DELETE_COMPANY_ADMIN, userVo.getUsername(),
                        employerCompanyName);
                sendMessage(shortMessage, detailedMessage);

                return;
            }

            // First, delete agent from the company
            CompanyVo employerComapany = companyServiceRemote.findByName(employerCompanyName);
            Set<UserVo> agents = employerComapany.getAgents();

            agents.remove(userVo);
            try {
                companyServiceRemote.save(employerComapany);
            } catch (InvalidCompanyDataException ex) {
                log.error("Could not update company data for {}", employerCompanyName);
            }
        }

        userServiceRemote.delete(userVo.getId());
        init();
        sendMessage(localeManager.localize(CHANGING_SUCCESS),
                localeManager.localize(DELETE_SUCCESS, userVo.getUsername()));
    }

    public void changeUserStatus(@NonNull final UserVo userVo) {
        userServiceRemote.changeStatus(userVo.getId());
        init();

        String detailedMessage = userVo.isActive() ? localeManager.localize(INACTIVATE_SUCCESS)
                : localeManager.localize(ACTIVATE_SUCCESS) + userVo.getUsername();
        sendMessage(localeManager.localize(CHANGING_SUCCESS), detailedMessage);
    }

    public void resetUserPassword(@NonNull final UserVo userVo) {
        userServiceRemote.resetPassword(userVo.getId());
        init();
        sendMessage(localeManager.localize(CHANGING_SUCCESS),
                localeManager.localize(RESET_PASSWORD_SUCCESS) + userVo.getUsername());
    }

    private void sendMessage(final String shortMessage, final String detailedMessage) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(shortMessage, detailedMessage));
    }

}
