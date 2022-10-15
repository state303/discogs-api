package io.dsub.discogs.api.master.repository;

import io.dsub.discogs.api.master.model.Master;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MasterRepository extends R2dbcRepository<Master, Long> {
    @Query("INSERT INTO master(id, data_quality, title, year) " +
            "VALUES (:#{[0].id}, :#{[0].dataQuality}, :#{[0].title}, :#{[0].year}) " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "last_modified_at=now()," +
            "data_quality=:#{[0].dataQuality}," +
            "title=:#{[0].title}," +
            "year=:#{[0].year} " +
            "WHERE id=:#{[0].id}")
    Mono<Master> saveOrUpdate(Master master);

    Flux<Master> findAllBy(Pageable pageable);
}
