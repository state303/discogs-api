package io.dsub.discogs.api.style.repository;

import io.dsub.discogs.api.style.model.Style;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface StyleRepository extends R2dbcRepository<Style, String> {
    @Query("INSERT INTO style(name) " +
            "VALUES ($1) " +
            "ON CONFLICT (name) DO NOTHING")
    Mono<Style> saveOrUpdate(String name);

    Flux<Style> findAllBy(Pageable pageable);
}
