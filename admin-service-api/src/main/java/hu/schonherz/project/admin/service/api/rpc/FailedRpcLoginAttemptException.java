package hu.schonherz.project.admin.service.api.rpc;

public class FailedRpcLoginAttemptException extends Exception {

    public FailedRpcLoginAttemptException(String message) {
        super(message);
    }

    public FailedRpcLoginAttemptException(String message, Throwable cause) {
        super(message, cause);
    }

}
