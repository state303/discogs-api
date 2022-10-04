package io.dsub.discogs.api.genre.repository;

import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.model.Genre;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GenreRepository extends R2dbcRepository<Genre, String> {

    @Query("INSERT INTO genre(name, created_at, last_modified_at) " +
            "VALUES (:#{[0].name}, now(), now()) " +
            "ON CONFLICT (name) DO NOTHING")
    Mono<Genre> insert(GenreCommand.Create command);
}
