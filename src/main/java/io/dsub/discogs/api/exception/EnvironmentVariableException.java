package io.dsub.discogs.api.exception;

public class EnvironmentVariableException extends BaseCheckedException {
    public EnvironmentVariableException(String message) {
        super(message);
    }

    public EnvironmentVariableException(String message, Throwable cause) {
        super(message, cause);
    }
}
