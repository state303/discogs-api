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
            "VALUES (:#{[0].id}, now(), now(), :#{[0].contactInfo}, :#{[0].dataQuality}, :#{[0].name}, :#{[0].profile}) " +
            "ON CONFLICT DO UPDATE SET " +
            "last_modified_at=now(), " +
            "contact_info=excluded.contact_info, " +
            "data_quality=excluded.data_quality, " +
            "name=excluded.name, " +
            "profile=excluded.profile")
    Mono<Label> saveOrUpdate(LabelCommand.Create command);
}
