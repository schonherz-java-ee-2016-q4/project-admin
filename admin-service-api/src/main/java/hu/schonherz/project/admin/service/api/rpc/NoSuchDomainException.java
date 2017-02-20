package hu.schonherz.project.admin.service.api.rpc;

public class NoSuchDomainException extends Exception {
    
    public NoSuchDomainException(final String message) {
        super(message);
    }

    public NoSuchDomainException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
