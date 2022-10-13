package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.style.command.StyleCommand;
import io.dsub.discogs.api.style.dto.StyleDTO;
import io.dsub.discogs.api.style.model.Style;
import io.dsub.discogs.api.style.repository.StyleRepository;
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
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;
    private final Validator validator;
    private final Function<Style, Mono<StyleDTO>> toDTO = style -> Mono.just(style.toDTO());

    @Override
    public Flux<StyleDTO> findAll() {
        return styleRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<StyleDTO>> findAll(Pageable pageable) {
        Flux<StyleDTO> sortedDTOs = styleRepository.findAll(pageable.getSort()).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTOs);
    }

    @Override
    public Mono<StyleDTO> save(StyleCommand.Create command) {
        return validate(command)
                .flatMap(styleRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(StyleCommand.Delete command) {
        return validate(command)
                .flatMap(validatedCommand -> Mono.just(validatedCommand.getName()))
                .flatMap(styleRepository::deleteById);
    }

    public Mono<Long> count() {
        return styleRepository.count();
    }

    @Override
    public <T> Mono<T> validate(T item) {
        return this.validator.validate(item);
    }
}
