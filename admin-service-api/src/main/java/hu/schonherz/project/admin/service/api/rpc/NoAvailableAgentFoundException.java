package hu.schonherz.project.admin.service.api.rpc;

public class NoAvailableAgentFoundException extends Exception {

    public NoAvailableAgentFoundException(final String message) {
        super(message);
    }

    public NoAvailableAgentFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
