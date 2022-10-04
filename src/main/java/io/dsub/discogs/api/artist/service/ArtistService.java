package io.dsub.discogs.api.artist.service;

import static io.dsub.discogs.api.artist.command.ArtistCommand.UpdateArtistCommand;
import static io.dsub.discogs.api.artist.command.ArtistCommand.CreateArtistCommand;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface ArtistService {
    Mono<Page<ArtistDTO>> getArtistsByPageAndSize(Pageable pageable);

    Mono<ArtistDTO> updateOrInsert(CreateArtistCommand command);

    Mono<ArtistDTO> update(int id, UpdateArtistCommand command);

    Mono<Void> delete(int id);

    Mono<ArtistDTO> findById(int id);
}
