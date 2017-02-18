package hu.schonherz.project.admin.service.api.rpc;

public class FailedRpcLogoutException extends Exception {

    public FailedRpcLogoutException(String message) {
        super(message);
    }

    public FailedRpcLogoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
