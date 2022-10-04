package io.dsub.discogs.api.database;

import org.springframework.core.io.Resource;

public interface DatabaseInitSchemaResourceLocator {
    Resource getInitSchemaResourceFrom(DatabaseType type);
}
