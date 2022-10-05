package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.genre.model.Genre;
import io.dsub.discogs.api.genre.repository.GenreRepository;
import io.dsub.discogs.api.util.PageUtil;
import io.dsub.discogs.api.validator.ReactiveValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final ReactiveValidator validator;

    @Override
    public Flux<GenreDTO> findAll() {
        return genreRepository.findAll().flatMap(Genre::toDTO);
    }

    @Override
    public Mono<Page<GenreDTO>> findAll(Pageable pageable) {
        final Pageable notNullPageable = PageUtil.getOrDefaultPageable(pageable);
        return genreRepository.findAllByNameNotNullOrderByNameAsc(pageable)
                .flatMap(Genre::toDTO)
                .collectList()
                .zipWith(genreRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), notNullPageable, tuple.getT2()));
    }

    @Override
    public Mono<GenreDTO> save(GenreCommand.Create command) {
        return validator.validate(command)
                .flatMap(genreRepository::insert)
                .flatMap(Genre::toDTO);
    }

    @Override
    public Mono<Void> delete(GenreCommand.Delete command) {
        return validator.validate(command)
                .flatMap(validatedCommand -> genreRepository.deleteById(validatedCommand.getName()));
    }
}
