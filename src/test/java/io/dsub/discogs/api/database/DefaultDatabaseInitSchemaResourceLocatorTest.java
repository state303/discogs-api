package io.dsub.discogs.api.database;

import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultDatabaseInitSchemaResourceLocatorTest {

    DefaultDatabaseInitSchemaResourceLocator locator =
            new DefaultDatabaseInitSchemaResourceLocator();

    @Test
    void getInitSchemaResourceFromReturnsResource() throws UnsupportedDatabaseException {
        Resource resource = locator.getInitSchemaResourceFrom(DatabaseType.POSTGRESQL);
        assertNotNull(resource);
    }

    @Test
    void getInitSchemaResourceFromThrows() {
        assertThrows(UnsupportedDatabaseException.class,
                () -> locator.getInitSchemaResourceFrom(DatabaseType.ORACLE));
    }
}
