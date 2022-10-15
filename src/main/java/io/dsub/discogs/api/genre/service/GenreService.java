package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService extends PagingService {

    Flux<GenreDTO> findAll();

    Mono<Page<GenreDTO>> findAllByPage(Pageable pageable);

    Mono<GenreDTO> save(String name);

    Mono<Void> delete(String name);
}
