package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.style.command.StyleCommand;
import io.dsub.discogs.api.style.dto.StyleDTO;
import io.dsub.discogs.api.style.model.Style;
import io.dsub.discogs.api.style.repository.StyleRepository;
import io.dsub.discogs.api.util.PageUtil;
import io.dsub.discogs.api.validator.ReactiveValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;
    private final ReactiveValidator validator;

    @Override
    public Flux<StyleDTO> findAll() {
        return styleRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<StyleDTO>> findAll(Pageable pageable) {
        final Pageable notNullPageable = PageUtil.getOrDefaultPageable(pageable);
        return styleRepository.findAllByNameNotNullOrderByNameAsc(pageable)
                .flatMap(toDTO)
                .collectList()
                .zipWith(styleRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), notNullPageable, tuple.getT2()));
    }

    @Override
    public Mono<StyleDTO> save(StyleCommand.Create command) {
        return validator.validate(command)
                .flatMap(styleRepository::insert)
                .flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(StyleCommand.Delete command) {
        return validator.validate(command)
                .flatMap(validatedCommand -> Mono.just(validatedCommand.getName()))
                .flatMap(styleRepository::deleteById);
    }

    private final Function<Style, Mono<StyleDTO>> toDTO = style -> Mono.just(style.toDTO());
}
