package io.dsub.discogs.api.artist.service;

import static io.dsub.discogs.api.artist.command.ArtistCommand.Update;
import static io.dsub.discogs.api.artist.command.ArtistCommand.Create;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Mono<Page<ArtistDTO>> getArtists(Pageable pageable);

    Flux<ArtistDTO> getArtists();

    Mono<ArtistDTO> saveOrUpdate(Create command);

    Mono<ArtistDTO> update(int id, Update command);

    Mono<Void> delete(int id);

    Mono<ArtistDTO> findById(int id);
}
