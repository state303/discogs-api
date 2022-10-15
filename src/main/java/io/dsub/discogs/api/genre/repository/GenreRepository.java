package io.dsub.discogs.api.genre.repository;

import io.dsub.discogs.api.genre.model.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GenreRepository extends R2dbcRepository<Genre, String> {
    @Query("INSERT INTO genre(name) " +
            "VALUES (:#{[0].name}) " +
            "ON CONFLICT (name) DO NOTHING")
    Mono<Genre> saveOrUpdate(Genre genre);

    Flux<Genre> findAllBy(Pageable pageable);
}
