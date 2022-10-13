package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import io.dsub.discogs.api.artist.command.ArtistCommand.Update;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.core.exception.ItemNotFoundException;
import io.dsub.discogs.api.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    private final Validator validator;
    private final Function<Artist, Mono<ArtistDTO>> toDTO = artist -> Mono.just(artist.toDTO());
    private final Predicate<Long> greaterThanZero = i -> i > 0;
    private final Function<Create, Mono<Artist>> createCommandToArtistMono = create -> Mono.just(create)
            .map(c -> Artist.builder()
                    .id(c.getId())
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .name(c.getName())
                    .realName(c.getRealName())
                    .profile(c.getProfile())
                    .dataQuality(c.getDataQuality())
                    .build());

    @Override
    public Mono<Page<ArtistDTO>> getArtists(Pageable pageable) {
        Flux<ArtistDTO> sortedDTOs =  artistRepository.findAll(pageable.getSort()).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTOs);
    }

    @Override
    public Flux<ArtistDTO> getArtists() {
        return artistRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<ArtistDTO> upsert(Create command) {
        return validate(command)
                .flatMap(createCommandToArtistMono)
                .flatMap(artistRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<ArtistDTO> update(long id, Update command) {
        return validate(command)
                .then(artistRepository.findById(id))
                .flatMap(artist -> Mono.just(artist.withMutableDataFrom(command)))
                .flatMap(artistRepository::save)
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(ItemNotFoundException.getInstance()));
    }

    @Override
    public Mono<Void> delete(long id) {
        return Mono.just(id)
                .filter(greaterThanZero)
                .flatMap(artistRepository::deleteById);
    }

    @Override
    public Mono<ArtistDTO> findById(long id) {
        return artistRepository.findById(id)
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(ItemNotFoundException.getInstance()));
    }

    public Mono<Long> count() {
        return artistRepository.count();
    }

    @Override
    public <T> Mono<T> validate(T item) {
        return validator.validate(item);
    }
}
