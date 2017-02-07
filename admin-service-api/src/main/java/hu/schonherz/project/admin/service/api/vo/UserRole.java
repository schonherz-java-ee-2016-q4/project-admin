package hu.schonherz.project.admin.service.api.vo;

import lombok.NonNull;

public enum UserRole {

    ADMIN, COMPANY_ADMIN, AGENT;

    public static UserRole fromString(@NonNull final String role) {
        switch (role.trim().toUpperCase()) {
            case "ADMIN":
                return ADMIN;
            case "COMPANY_ADMIN":
                return COMPANY_ADMIN;
            case "AGENT":
                return AGENT;
            default:
                throw new IllegalArgumentException("Role " + role + " does not exist!");
        }

    }

}
