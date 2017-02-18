package hu.schonherz.project.admin.service.api.service.user;

import lombok.NonNull;

public class InvalidUserDataException extends Exception {

    public InvalidUserDataException(@NonNull final String message) {
        super(message);
    }

    public InvalidUserDataException(@NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
    }

}
