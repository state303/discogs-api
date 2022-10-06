package io.dsub.discogs.api.core.exception;

public class EnvironmentVariableException extends BaseCheckedException {
    public EnvironmentVariableException(String message) {
        super(message);
    }

    public EnvironmentVariableException(String message, Throwable cause) {
        super(message, cause);
    }
}
