package io.dsub.discogs.api.release.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.core.service.ValidatingService;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import reactor.core.publisher.Mono;

import static io.dsub.discogs.api.release.command.ReleaseCommand.*;

public interface ReleaseService extends PagingService, ValidatingService {
    Mono<ReleaseDTO> createOrUpdate(Create command);
    Mono<ReleaseDTO> update(Update command);
    Mono<ReleaseDTO> delete(DeleteByID command);
}