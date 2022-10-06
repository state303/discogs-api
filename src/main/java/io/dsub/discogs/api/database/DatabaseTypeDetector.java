package io.dsub.discogs.api.database;

import io.dsub.discogs.api.core.exception.InvalidR2DBCConnectionUrlException;
import io.dsub.discogs.api.core.exception.UnsupportedDatabaseException;

public interface DatabaseTypeDetector {
    DatabaseType findTypeFrom(String url) throws UnsupportedDatabaseException, InvalidR2DBCConnectionUrlException;
}
