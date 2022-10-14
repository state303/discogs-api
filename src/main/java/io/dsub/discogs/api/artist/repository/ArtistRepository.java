package io.dsub.discogs.api.artist.repository;

import io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import io.dsub.discogs.api.artist.model.Artist;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistRepository extends R2dbcRepository<Artist, Long> {
    @Query("INSERT INTO artist (id, data_quality, name, profile, real_name) " +
            "VALUES (:#{[0].id}, :#{[0].dataQuality}, :#{[0].name}, :#{[0].profile}, :#{[0].realName}) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "last_modified_at=now(), " +
            "data_quality=:#{[0].dataQuality}, " +
            "name=:#{[0].name}, " +
            "profile=:#{[0].profile}, " +
            "real_name=:#{[0].realName} " +
            "WHERE artist.id=:#{[0].id}")
    Mono<Artist> saveOrUpdate(Artist artist);
}
