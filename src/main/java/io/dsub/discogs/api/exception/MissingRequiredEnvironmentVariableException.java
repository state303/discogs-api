package io.dsub.discogs.api.exception;

public class MissingRequiredEnvironmentVariableException extends EnvironmentVariableException {

    private static String createMessageFromEnvironmentKey(String key) {
        return String.format("missing required environment variable: %s", key);
    }

    public MissingRequiredEnvironmentVariableException(String key) {
        super(createMessageFromEnvironmentKey(key));
    }

    public MissingRequiredEnvironmentVariableException(String key, Throwable cause) {
        super(createMessageFromEnvironmentKey(key), cause);
    }
}
