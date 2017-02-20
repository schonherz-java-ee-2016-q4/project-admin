package hu.schonherz.project.admin.service.api.rpc;

public class UsernameNotFoundException extends Exception {

    public UsernameNotFoundException(final String message) {
        super(message);
    }

    public UsernameNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
