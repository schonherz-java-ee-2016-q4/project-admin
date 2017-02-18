package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;
import java.util.Objects;

public class UserData implements Serializable {

    private static final long serialVersionUID = 10L;

    private Long id;

    private String email;

    private String username;

    private String password;

    private UserRole userRole;

    private String fullName;

    private Gender gender;

    private String companyName;

    private String phone;

    private String picUrl;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(final UserRole userRole) {
        this.userRole = userRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int hashCode() {
        final int mulplier1 = 3;
        final int mulplier2 = 29;
        int hash = mulplier1;
        hash = mulplier2 * hash + Objects.hashCode(this.id);
        hash = mulplier2 * hash + Objects.hashCode(this.email);
        hash = mulplier2 * hash + Objects.hashCode(this.username);
        hash = mulplier2 * hash + Objects.hashCode(this.password);
        hash = mulplier2 * hash + Objects.hashCode(this.userRole);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserData other = (UserData) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }

        return this.userRole == other.userRole;
    }

    @Override
    public String toString() {
        return "UserData{" + "id=" + id + ", email=" + email + ", username=" + username + ", password=" + password + ", userRole=" + userRole + '}';
    }

}
