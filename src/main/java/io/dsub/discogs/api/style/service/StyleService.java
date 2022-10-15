package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.style.command.StyleCommand;
import io.dsub.discogs.api.style.dto.StyleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StyleService extends PagingService {
    Flux<StyleDTO> findAll();
    Mono<Page<StyleDTO>> findAll(Pageable pageable);
    Mono<StyleDTO> upsert(String name);
    Mono<Void> delete(String name);
}
