package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.exception.NoSuchElementException;
import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import io.dsub.discogs.api.label.model.Label;
import io.dsub.discogs.api.label.repository.LabelRepository;
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
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final Validator validator;
    private final Function<Label, Mono<LabelDTO>> toDTO = label -> Mono.just(label.toDTO());

    @Override
    public Flux<LabelDTO> getLabels() {
        return labelRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<LabelDTO>> getLabels(Pageable pageable) {
        Flux<LabelDTO> sortedDTOs = labelRepository.findAll(pageable.getSort()).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTOs);
    }

    @Override
    public Mono<LabelDTO> updateLabel(Integer id, LabelCommand.Update command) {
        return validate(command)
                .then(labelRepository.findById(id))
                .flatMap(label -> Mono.just(label.withMutableDataFrom(command)))
                .flatMap(labelRepository::save)
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    @Override
    public Mono<LabelDTO> saveOrUpdate(LabelCommand.Create command) {
        return validate(command)
                .flatMap(labelRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<Void> deleteLabel(Integer id) {
        return labelRepository.deleteById(id);
    }

    @Override
    public Mono<LabelDTO> findById(Integer id) {
        return labelRepository
                .findById(id)
                .flatMap(toDTO)
                .switchIfEmpty(Mono.error(NoSuchElementException.getInstance()));
    }

    private Mono<Long> count() {
        return labelRepository.count();
    }

    @Override
    public <T> Mono<T> validate(T item) {
        return this.validator.validate(item);
    }
}
