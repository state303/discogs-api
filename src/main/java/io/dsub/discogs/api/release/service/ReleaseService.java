package io.dsub.discogs.api.release.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.core.service.ValidatingService;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.dsub.discogs.api.release.command.ReleaseCommand.*;

public interface ReleaseService extends PagingService, ValidatingService {
    Flux<ReleaseDTO> findAll();
    Mono<Page<ReleaseDTO>> findAllByPage(Pageable pageable);
    Mono<ReleaseDTO> findById(long id);
    Mono<ReleaseDTO> upsert(Create command);
    Mono<ReleaseDTO> update(long id, Update command);
    Mono<Void> delete(long id);
}