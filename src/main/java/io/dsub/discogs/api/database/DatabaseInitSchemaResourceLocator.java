package io.dsub.discogs.api.database;

import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import org.springframework.core.io.Resource;

public interface DatabaseInitSchemaResourceLocator {
    Resource getInitSchemaResourceFrom(DatabaseType type) throws UnsupportedDatabaseException;
}
