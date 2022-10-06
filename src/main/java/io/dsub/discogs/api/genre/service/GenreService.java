package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.core.service.ValidatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService extends ValidatingService, PagingService {

    Flux<GenreDTO> findAll();

    Mono<Page<GenreDTO>> findAll(Pageable pageable);

    Mono<GenreDTO> save(GenreCommand.Create command);

    Mono<Void> delete(GenreCommand.Delete command);
}
