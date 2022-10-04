package io.dsub.discogs.api.database;

import io.dsub.discogs.api.exception.InvalidR2DBCConnectionUrlException;
import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import org.springframework.stereotype.Component;

/**
 * R2DBCDatabaseTypeDetector finds driver(a database) from given connection url string.
 */
@Component
public class R2DBCDatabaseTypeDetector implements DatabaseTypeDetector {
    @Override
    public DatabaseType findTypeFrom(String url) throws UnsupportedDatabaseException, InvalidR2DBCConnectionUrlException {
        String driverName = parseDatabaseNameUpperCase(url);
        return getDatabaseTypeFromName(driverName);
    }

    private DatabaseType getDatabaseTypeFromName(String driverName) throws UnsupportedDatabaseException {
        try {
            return DatabaseType.valueOf(driverName);
        } catch (IllegalArgumentException ignored) {
            throw new UnsupportedDatabaseException(driverName);
        }
    }

    private String parseDatabaseNameUpperCase(String url) throws InvalidR2DBCConnectionUrlException {
        if (url == null) {
            throw new InvalidR2DBCConnectionUrlException();
        }
        final String trimmed = url.trim();
        if (!trimmed.startsWith("r2dbc:")) {
            throw new InvalidR2DBCConnectionUrlException();
        }

        String[] parts = trimmed.split(":");
        if (parts.length < 3) {
            throw new InvalidR2DBCConnectionUrlException();
        }
        return parts[1].toUpperCase();
    }
}
