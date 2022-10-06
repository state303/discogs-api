package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import io.dsub.discogs.api.label.model.Label;
import io.dsub.discogs.api.label.repository.LabelRepository;
import io.dsub.discogs.api.service.PagingService;
import io.dsub.discogs.api.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;


// TODO: implement me!
@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final Validator validator;

    @Override
    public Flux<LabelDTO> getLabels() {
        return labelRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<LabelDTO>> getLabels(Pageable pageable) {
        return getPagedResult(count(), pageable, labelRepository.findAll(pageable.getSort()).flatMap(toDTO));
    }

    @Override
    public Mono<LabelDTO> updateLabel(LabelCommand.Update command) {
        return null;
    }

    @Override
    public Mono<LabelDTO> saveOrUpdate(LabelCommand.Create command) {
        return null;
    }

    @Override
    public Mono<Void> deleteLabel(Integer id) {
        return null;
    }

    private Mono<Long> count() {
        return labelRepository.count();
    }

    private final Function<Label, Mono<LabelDTO>> toDTO = label -> Mono.just(label.toDTO());

    @Override
    public <T> Mono<T> validate(T item) {
        return this.validator.validate(item);
    }
}
