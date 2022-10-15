package io.dsub.discogs.api.release.repository;

import io.dsub.discogs.api.release.model.Release;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReleaseRepository extends R2dbcRepository<Release, Long> {
    @Query("INSERT INTO release (id, country, data_quality, has_valid_day, has_valid_month, has_valid_year, is_master, master_id, listed_release_date, notes, release_date, status, title) " +
            "VALUES (:#{[0].id}, :#{[0].country}, :#{[0].dataQuality}, :#{[0].hasValidDay}, :#{[0].hasValidMonth}, :#{[0].hasValidYear}, :#{[0].isMaster}, :#{[0].masterId}, :#{[0].listedReleaseDate}, :#{[0].notes}, :#{[0].releaseDate}, :#{[0].status}, :#{[0].title}) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "last_modified_at=now()," +
            "country=:#{[0].country}," +
            "data_quality=:#{[0].dataQuality}," +
            "has_valid_day=:#{[0].hasValidDay}," +
            "has_valid_month=:#{[0].hasValidMonth}," +
            "has_valid_year=:#{[0].hasValidYear}," +
            "is_master=:#{[0].isMaster}," +
            "master_id=:#{[0].masterId}," +
            "listed_release_date=:#{[0].listedReleaseDate}," +
            "notes=:#{[0].notes}," +
            "release_date=:#{[0].releaseDate}," +
            "status=:#{[0].status}," +
            "title=:#{[0].title} " +
            "WHERE id=:#{[0].id}")
    Mono<Release> saveOrUpdate(Release release);

    Flux<Release> findAllBy(Pageable pageable);
}
