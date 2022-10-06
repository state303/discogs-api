package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import io.dsub.discogs.api.artist.command.ArtistCommand.Update;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.exception.NoSuchElementException;
import io.dsub.discogs.api.util.PageUtil;
import io.dsub.discogs.api.validator.ReactiveValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    private final ReactiveValidator validator;

    @Override
    public Mono<Page<ArtistDTO>> getArtists(Pageable pageable) {
        final Pageable notNullPageable = PageUtil.getOrDefaultPageable(pageable);
        return artistRepository.findAllByNameNotNullOrderByNameAscIdAsc(notNullPageable)
                .flatMap(toDTO)
                .collectList()
                .zipWith(artistRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), notNullPageable, tuple.getT2()));
    }

    @Override
    public Flux<ArtistDTO> getArtists() {
        return artistRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<ArtistDTO> saveOrUpdate(Create command) {
        return validator.validate(command)
                .flatMap(artistRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<ArtistDTO> update(int id, Update command) {
        return artistRepository.findById(id)
                .flatMap(artist -> updateArtistWithCommand(artist, command))
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    @Override
    public Mono<Void> delete(int id) {
        return Mono.just(id)
                .filter(greaterThanZero)
                .flatMap(artistRepository::deleteById);
    }

    @Override
    public Mono<ArtistDTO> findById(int id) {
        return artistRepository.findById(id)
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    private final Function<Artist, Mono<ArtistDTO>> toDTO = artist -> Mono.just(artist.toDTO());

    private Mono<Artist> updateArtistWithCommand(Artist artist, Update command) {
        return this.artistRepository.save(artist.withMutableDataFrom(command));
    }

    private final Predicate<Integer> greaterThanZero = i -> i > 0;
}
