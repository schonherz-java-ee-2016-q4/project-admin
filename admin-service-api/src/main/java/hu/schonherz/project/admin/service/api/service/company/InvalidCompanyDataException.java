package hu.schonherz.project.admin.service.api.service.company;

import lombok.NonNull;

public class InvalidCompanyDataException extends Exception {

    public InvalidCompanyDataException(@NonNull final String message) {
        super(message);
    }

    public InvalidCompanyDataException(@NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
    }

}
