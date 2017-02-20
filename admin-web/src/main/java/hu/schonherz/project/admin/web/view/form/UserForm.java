package hu.schonherz.project.admin.web.view.form;

import hu.schonherz.project.admin.service.api.vo.UserRole;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class UserForm {

    private Long id;
    private String email;
    private String companyName;
    private String username;
    private String password;
    private boolean active;
    private boolean loggedIn;
    private boolean available;
    private UserRole userRole;

    public UserForm(@NonNull final UserVo vo) {
        id = vo.getId();
        email = vo.getEmail();
        companyName = vo.getCompanyName();
        username = vo.getUsername();
        password = vo.getPassword();
        active = vo.isActive();
        loggedIn = vo.isLoggedIn();
        available = vo.isAvailable();
        userRole = vo.getUserRole();
    }

    public UserVo getUserVo() {
        UserVo vo = new UserVo();
        vo.setId(id);
        vo.setEmail(email);
        vo.setCompanyName(companyName);
        vo.setUsername(username);
        vo.setPassword(password);
        vo.setUserRole(userRole);

        vo.setActive(active);
        vo.setLoggedIn(loggedIn);
        vo.setAvailable(available);

        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
