package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.style.dto.StyleDTO;
import io.dsub.discogs.api.style.model.Style;
import io.dsub.discogs.api.style.repository.StyleRepository;
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
    public Mono<StyleDTO> upsert(String name) {
        return styleRepository.saveOrUpdate(name).flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(String name) {
        return styleRepository.deleteById(name);
    }

    public Mono<Long> count() {
        return styleRepository.count();
    }
}
