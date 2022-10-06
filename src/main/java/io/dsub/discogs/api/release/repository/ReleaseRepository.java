package io.dsub.discogs.api.release.repository;

import io.dsub.discogs.api.release.model.Release;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends R2dbcRepository<Release, Long> {
}
