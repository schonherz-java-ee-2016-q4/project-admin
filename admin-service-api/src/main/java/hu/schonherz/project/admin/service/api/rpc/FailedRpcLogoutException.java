package hu.schonherz.project.admin.service.api.rpc;

public class FailedRpcLogoutException extends Exception {

    public FailedRpcLogoutException(final String message) {
        super(message);
    }

    public FailedRpcLogoutException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
