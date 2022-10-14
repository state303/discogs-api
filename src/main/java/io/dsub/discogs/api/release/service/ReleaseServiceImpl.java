package io.dsub.discogs.api.release.service;

import io.dsub.discogs.api.parser.ReleaseDateParser;
import io.dsub.discogs.api.parser.ReleaseDateParserImpl;
import io.dsub.discogs.api.release.command.ReleaseCommand;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import io.dsub.discogs.api.release.model.Release;
import io.dsub.discogs.api.release.repository.ReleaseRepository;
import io.dsub.discogs.api.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository repository;
    private final Validator validator;

    private final ReleaseDateParser parser = new ReleaseDateParserImpl();
    private final Function<Release, Mono<ReleaseDTO>> toReleaseDTO =
            release -> Mono.just(release.toDTO());

    @Override
    public <T> Mono<T> validate(T item) {
        return validator.validate(item);
    }

    @Override
    public Flux<ReleaseDTO> findAll() {
        return repository.findAll().flatMap(toReleaseDTO);
    }

    @Override
    public Mono<Page<ReleaseDTO>> findAllByPage(Pageable pageable) {
        Flux<ReleaseDTO> dtoFlux = repository.findAll(pageable.getSort()).flatMap(toReleaseDTO);
        return getPagedResult(repository.count(), pageable, dtoFlux);
    }

    @Override
    public Mono<ReleaseDTO> findById(long id) {
        return repository.findById(id).flatMap(toReleaseDTO);
    }

    @Override
    public Mono<ReleaseDTO> upsert(ReleaseCommand.Create command) {
        return null;
    }

    @Override
    public Mono<ReleaseDTO> update(ReleaseCommand.Update command) {
        return null;
    }

    @Override
    public Mono<ReleaseDTO> delete(ReleaseCommand.DeleteByID command) {
        return null;
    }
}
