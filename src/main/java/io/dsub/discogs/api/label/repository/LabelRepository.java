package io.dsub.discogs.api.label.repository;

import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.model.Label;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LabelRepository extends R2dbcRepository<Label, Long> {
    @Query("INSERT INTO label(id, created_at, last_modified_at, contact_info, data_quality, name, profile) " +
            "VALUES (:#{[0].id}, :#{[0].createdAt}, :#{[0].lastModifiedAt}, :#{[0].contactInfo}, :#{[0].dataQuality}, :#{[0].name}, :#{[0].profile}) " +
            "ON CONFLICT DO UPDATE SET " +
            "created_at=:#{[0].createdAt}, " +
            "last_modified_at=:#{[0].lastModifiedAt}, " +
            "contact_info=:#{[0].contactInfo}, " +
            "data_quality=:#{[0].dataQuality}, " +
            "name=:#{[0].name}, " +
            "profile=:#{[0].profile}")
    Mono<Label> saveOrUpdate(Label label);
}
