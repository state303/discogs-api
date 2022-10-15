package io.dsub.discogs.api.label.service;

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

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final Function<Label, Mono<LabelDTO>> toDTO =
            label -> Mono.just(label.toDTO());

    private final Function<LabelCommand.Create, Mono<Label>> createCommandToLabelMono =
            command -> Mono.just(command)
                    .zipWith(Mono.just(LocalDateTime.now()))
                    .flatMap(tuple -> Mono.just(Label.builder()
                            .createdAt(tuple.getT2())
                            .lastModifiedAt(tuple.getT2())
                            .id(tuple.getT1().getId())
                            .name(tuple.getT1().getName())
                            .contactInfo(tuple.getT1().getContactInfo())
                            .profile(tuple.getT1().getProfile())
                            .dataQuality(tuple.getT1().getDataQuality())
                            .parentLabelID(tuple.getT1().getParentLabelId())
                            .build()));

    @Override
    public Flux<LabelDTO> findAll() {
        return labelRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<LabelDTO>> findAllByPage(Pageable pageable) {
        Flux<LabelDTO> sortedDTOs = labelRepository.findAllBy(pageable).flatMap(toDTO);
        return getPagedResult(count(), pageable, sortedDTOs);
    }

    @Override
    public Mono<LabelDTO> update(long id, LabelCommand.Update command) {
        return labelRepository.findById(id)
                .flatMap(label -> Mono.just(label.withMutableDataFrom(command)))
                .flatMap(labelRepository::save)
                .flatMap(toDTO);
    }

    @Override
    public Mono<LabelDTO> upsert(LabelCommand.Create command) {
        return Mono.just(command)
                .flatMap(createCommandToLabelMono)
                .flatMap(labelRepository::saveOrUpdate)
                .flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(long id) {
        return labelRepository.deleteById(id);
    }

    @Override
    public Mono<LabelDTO> findById(long id) {
        return labelRepository
                .findById(id)
                .flatMap(toDTO);
    }
    private Mono<Long> count() {
        return labelRepository.count();
    }
}
