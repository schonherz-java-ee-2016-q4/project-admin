package hu.schonherz.project.admin.service.api.vo;

public enum UserRole {

    ADMIN, COMPANY_ADMIN, AGENT;

    public static UserRole fromString(final String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("role string must not be null or empty!");
        }

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
