package io.dsub.discogs.api.config;

import io.dsub.discogs.api.database.DatabaseInitSchemaResourceLocator;
import io.dsub.discogs.api.database.DatabaseType;
import io.dsub.discogs.api.database.DatabaseTypeDetector;
import io.dsub.discogs.api.exception.EnvironmentVariableException;
import io.dsub.discogs.api.exception.InvalidR2DBCConnectionUrlException;
import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.DatabasePopulator;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {
    private final DatabaseTypeDetector typeDetector;
    private final DatabaseInitSchemaResourceLocator schemaResourceLocator;

    @Bean
    public DatabaseProperties databaseProperties(Environment env) {
        return new DatabaseProperties(env);
    }

    @Bean
    public ConnectionFactory connectionFactory(DatabaseProperties properties) throws EnvironmentVariableException {
        final ConnectionFactoryOptions options = getConnectionFactoryOptions(properties);
        return ConnectionFactories.get(options);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(
            @Qualifier("connectionFactory") ConnectionFactory connectionFactory, DatabaseProperties properties)
            throws UnsupportedDatabaseException, InvalidR2DBCConnectionUrlException {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        DatabasePopulator populator = getDatabasePopulator(properties);
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    private DatabasePopulator getDatabasePopulator(DatabaseProperties properties) throws UnsupportedDatabaseException, InvalidR2DBCConnectionUrlException {
        String url = properties.getUrl();
        DatabaseType type = typeDetector.findTypeFrom(url);
        Resource initSchemaResource = schemaResourceLocator.getInitSchemaResourceFrom(type);
        return new ResourceDatabasePopulator(initSchemaResource);
    }

    private ConnectionFactoryOptions getConnectionFactoryOptions(DatabaseProperties properties) throws EnvironmentVariableException {
        return ConnectionFactoryOptions.parse(properties.getUrl()).mutate()
                .option(ConnectionFactoryOptions.USER, properties.getUsername())
                .option(ConnectionFactoryOptions.PASSWORD, properties.getPassword())
                .build();
    }
}
