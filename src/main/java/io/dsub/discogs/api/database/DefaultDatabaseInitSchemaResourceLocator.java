package io.dsub.discogs.api.database;

import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DefaultDatabaseInitSchemaResourceLocator implements DatabaseInitSchemaResourceLocator {
    @Override
    public Resource getInitSchemaResourceFrom(DatabaseType type) throws UnsupportedDatabaseException {
        if (type == DatabaseType.POSTGRESQL) {
            return new ClassPathResource("postgres-schema.sql");
        }
        throw new UnsupportedDatabaseException(type.name());
    }
}
