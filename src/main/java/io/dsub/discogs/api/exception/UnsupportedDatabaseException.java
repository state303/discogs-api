package io.dsub.discogs.api.exception;

public class UnsupportedDatabaseException extends BaseCheckedException {

    private static String createMessageFromDriverName(String driverName) {
        return String.format("database %s is not supported", driverName);
    }

    public UnsupportedDatabaseException(String driverName) {
        super(createMessageFromDriverName(driverName));
    }

    public UnsupportedDatabaseException(String driverName, Throwable cause) {
        super(createMessageFromDriverName(driverName), cause);
    }
}
