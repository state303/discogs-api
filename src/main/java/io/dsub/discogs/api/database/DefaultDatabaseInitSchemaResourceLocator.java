package io.dsub.discogs.api.database;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DefaultDatabaseInitSchemaResourceLocator implements DatabaseInitSchemaResourceLocator {
    @Override
    public Resource getInitSchemaResourceFrom(DatabaseType type) {
        if (type == DatabaseType.POSTGRESQL) {
            return new ClassPathResource("postgres-schema.sql");
        }
        throw new UnsupportedOperationException("only postgresql is supported at this point");
    }
}
