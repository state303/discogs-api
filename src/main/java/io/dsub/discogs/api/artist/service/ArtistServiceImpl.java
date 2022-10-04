package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.command.ArtistCommand.CreateArtistCommand;
import io.dsub.discogs.api.artist.command.ArtistCommand.UpdateArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.exception.NoSuchElementException;
import io.dsub.discogs.api.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    @Override
    public Mono<Page<ArtistDTO>> getArtistsByPageAndSize(Pageable pageable) {
        final Pageable notNullPageable = PageUtil.getOrDefaultPageable(pageable);
        return artistRepository.findAllByNameNotNullOrderByNameAscIdAsc(PageUtil.getOrDefaultPageable(notNullPageable))
                .flatMap(Artist::toDTO)
                .collectList()
                .cache()
                .zipWith(artistRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), notNullPageable, tuple.getT2()));
    }

    @Override
    public Mono<ArtistDTO> updateOrInsert(CreateArtistCommand command) {
        return artistRepository.insertOrUpdate(command)
                .flatMap(Artist::toDTO);
    }

    @Override
    public Mono<ArtistDTO> update(int id, UpdateArtistCommand command) {
        return artistRepository.findById(id)
                .flatMap(artist -> saveArtistAfterUpdate(artist, command))
                .flatMap(mapToDTO())
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    @Override
    public Mono<Void> delete(int id) {
        return artistRepository.deleteById(id);
    }

    @Override
    public Mono<ArtistDTO> findById(int id) {
        return artistRepository.findById(id)
                .flatMap(mapToDTO())
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    private Function<Artist, Mono<ArtistDTO>> mapToDTO() {
        return Artist::toDTO;
    }

    private Mono<Artist> saveArtistAfterUpdate(Artist artist, UpdateArtistCommand command) {
        return this.artistRepository.save(artist.withMutableDataFrom(command));
    }
}
