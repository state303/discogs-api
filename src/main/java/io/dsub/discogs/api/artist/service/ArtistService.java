package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.core.service.ValidatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService extends PagingService, ValidatingService {
    Flux<ArtistDTO> findAll();
    Mono<Page<ArtistDTO>> findAllByPage(Pageable pageable);
    Mono<ArtistDTO> findById(long id);
    Mono<ArtistDTO> upsert(ArtistCommand.Create command);
    Mono<ArtistDTO> update(long id, ArtistCommand.Update command);
    Mono<Void> delete(long id);
}
