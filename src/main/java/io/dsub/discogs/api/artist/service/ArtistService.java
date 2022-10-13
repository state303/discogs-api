package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.core.service.ValidatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import static io.dsub.discogs.api.artist.command.ArtistCommand.Update;

public interface ArtistService extends ValidatingService, PagingService {
    Mono<Page<ArtistDTO>> getArtists(Pageable pageable);

    Flux<ArtistDTO> getArtists();

    Mono<ArtistDTO> upsert(Create command);

    Mono<ArtistDTO> update(long id, Update command);

    Mono<Void> delete(long id);

    Mono<ArtistDTO> findById(long id);
}
