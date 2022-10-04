package io.dsub.discogs.api.exception;

public class InvalidEnvironmentVariableException extends EnvironmentVariableException {
    private static String createMessageFromEnvironmentKey(String key) {
        return String.format("environment variable %s has invalid value", key);
    }

    public InvalidEnvironmentVariableException(String key) {
        super(createMessageFromEnvironmentKey(key));
    }

    public InvalidEnvironmentVariableException(String key, Throwable cause) {
        super(createMessageFromEnvironmentKey(key), cause);
    }
}
