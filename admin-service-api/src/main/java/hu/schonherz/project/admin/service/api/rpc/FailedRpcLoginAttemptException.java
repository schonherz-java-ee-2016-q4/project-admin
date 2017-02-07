package hu.schonherz.project.admin.service.api.rpc;

import lombok.NonNull;

public class FailedRpcLoginAttemptException extends Exception {

    public FailedRpcLoginAttemptException(@NonNull final String message) {
        super(message);
    }

    public FailedRpcLoginAttemptException(@NonNull final String message, @NonNull final Throwable cause) {
        super(message, cause);
    }

}
