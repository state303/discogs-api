package io.dsub.discogs.api.artist.repository;

import io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import io.dsub.discogs.api.artist.model.Artist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistRepository extends R2dbcRepository<Artist, Integer> {
    // @formatter:off
    @Query("INSERT INTO artist (id, created_at, last_modified_at, data_quality, name, profile, real_name) " +
            "VALUES (:#{[0].id}, now(), now(), :#{[0].dataQuality}, :#{[0].name}, :#{[0].profile}, :#{[0].realName}) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "last_modified_at=now(), " +
            "data_quality=excluded.data_quality, " +
            "name=excluded.name, " +
            "profile=excluded.profile, " +
            "real_name=excluded.real_name " +
            "WHERE artist.id=excluded.id")
    // @formatter:on
    Mono<Artist> insertOrUpdate(Create command);

    Flux<Artist> findAllByNameNotNullOrderByNameAscIdAsc(final Pageable page);
}
