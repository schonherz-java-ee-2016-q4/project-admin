package hu.schonherz.project.remote.admin.api.vo.issuetracker;

import java.io.Serializable;
import java.util.Objects;

public class RemoteUserVo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String username;
    private String encryptedPassword;
    private String employerCompanyName;
    private String role;

    public RemoteUserVo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmployerCompanyName() {
        return employerCompanyName;
    }

    public void setEmployerCompanyName(String employerCompanyName) {
        this.employerCompanyName = employerCompanyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RemoteUserVo{" + "username=" + username + ", encryptedPassword=" + encryptedPassword + ", employerCompanyName=" + employerCompanyName + ", role=" + role + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        final int base = 97;
        hash = base * hash + Objects.hashCode(this.username);
        hash = base * hash + Objects.hashCode(this.encryptedPassword);
        hash = base * hash + Objects.hashCode(this.employerCompanyName);
        hash = base * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RemoteUserVo other = (RemoteUserVo) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.encryptedPassword, other.encryptedPassword)) {
            return false;
        }
        if (!Objects.equals(this.employerCompanyName, other.employerCompanyName)) {
            return false;
        }

        return Objects.equals(this.role, other.role);
    }

}
