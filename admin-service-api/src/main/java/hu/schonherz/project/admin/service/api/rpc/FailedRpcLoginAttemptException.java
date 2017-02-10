package hu.schonherz.project.admin.service.api.rpc;

public class FailedRpcLoginAttemptException extends Exception {

    public FailedRpcLoginAttemptException(final String message) {
        super(message);
    }

    public FailedRpcLoginAttemptException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
