package io.dsub.discogs.api.label.repository;

import io.dsub.discogs.api.label.model.Label;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LabelRepository extends R2dbcRepository<Label, Long> {
    @Query("INSERT INTO label(id, contact_info, data_quality, name, profile, parent_label_id) " +
            "VALUES (:#{[0].id}, :#{[0].contactInfo}), :#{[0].dataQuality}, :#{[0].name}, :#{[0].profile}, :#{[0].parentLabelID} " +
            "ON CONFLICT (id) DO UPDATE SET " +
            "last_modified_at=now()," +
            "contact_info=:#{[0].contactInfo}," +
            "data_quality=:#{[0].dataQuality}," +
            "name=:#{[0].name}," +
            "profile=:#{[0].profile}," +
            "parent_label_id=:#{[0].parentLabelID} " +
            "WHERE id=:#{[0].id}")
    Mono<Label> saveOrUpdate(Label label);

    Flux<Label> findAllBy(Pageable pageable);
}