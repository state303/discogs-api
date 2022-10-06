package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import io.dsub.discogs.api.label.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository repository;

    @Override
    public Flux<LabelDTO> getLabels() {
        return null;
    }

    @Override
    public Mono<Page<LabelDTO>> getLabels(Pageable pageable) {
        return null;
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
}
