package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.genre.model.Genre;
import io.dsub.discogs.api.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final Function<Genre, Mono<GenreDTO>> toDTO =
            artist -> Mono.just(artist.toDTO());

    @Override
    public Flux<GenreDTO> findAll() {
        return genreRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<GenreDTO>> findAllByPage(Pageable pageable) {
        Flux<GenreDTO> sortedDTO = genreRepository.findAll(pageable.getSort()).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTO);
    }

    @Override
    public Mono<GenreDTO> save(String name) {
        return genreRepository.saveOrUpdate(Genre.builder().name(name).build()).flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(String name) {
        return genreRepository.deleteById(name);
    }

    public Mono<Long> count() {
        return this.genreRepository.count();
    }
}
