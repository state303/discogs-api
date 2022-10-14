package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.genre.model.Genre;
import io.dsub.discogs.api.genre.repository.GenreRepository;
import io.dsub.discogs.api.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final Validator validator;
    private final Function<Genre, Mono<GenreDTO>> toDTO =
            artist -> Mono.just(artist.toDTO());

    private final Function<GenreCommand.Create, Mono<Genre>> createGenreToGenre =
            cmd -> Mono.just(Genre.builder()
                    .createdAt(LocalDateTime.now())
                    .name(cmd.getName()).build());

    private final Function<GenreCommand.Delete, Mono<String>> getName =
            cmd -> Mono.just(cmd.getName());

    @Override
    public Flux<GenreDTO> findAll() {
        return genreRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<GenreDTO>> findAll(Pageable pageable) {
        Flux<GenreDTO> sortedDTO = genreRepository.findAll(pageable.getSort()).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTO);
    }

    @Override
    public Mono<GenreDTO> save(GenreCommand.Create command) {
        return validate(command)
                .flatMap(createGenreToGenre)
                .flatMap(genreRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(GenreCommand.Delete command) {
        return validate(command)
                .flatMap(getName)
                .flatMap(genreRepository::deleteById);
    }

    public Mono<Long> count() {
        return this.genreRepository.count();
    }
    @Override
    public <T> Mono<T> validate(T item) {
        return this.validator.validate(item);
    }
}
