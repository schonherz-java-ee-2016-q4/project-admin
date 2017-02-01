package hu.schonherz.project.admin.service.api.service.exception;

public class InvalidUserDataException extends Exception {

    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
