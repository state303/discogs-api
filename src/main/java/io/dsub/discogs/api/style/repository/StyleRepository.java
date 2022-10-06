package io.dsub.discogs.api.style.repository;

import io.dsub.discogs.api.style.command.StyleCommand;
import io.dsub.discogs.api.style.model.Style;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface StyleRepository extends R2dbcRepository<Style, String> {
    @Query("INSERT INTO style(name, created_at, last_modified_at) " +
            "VALUES (:#{[0].name}, now(), now()) " +
            "ON CONFLICT (name) DO NOTHING")
    Mono<Style> insert(StyleCommand.Create command);
}
