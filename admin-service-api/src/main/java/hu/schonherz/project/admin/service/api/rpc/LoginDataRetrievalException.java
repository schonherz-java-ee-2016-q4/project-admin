package hu.schonherz.project.admin.service.api.rpc;

public class LoginDataRetrievalException extends Exception {

    public LoginDataRetrievalException(final String message) {
        super(message);
    }

    public LoginDataRetrievalException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
