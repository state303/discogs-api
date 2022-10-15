package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LabelService extends PagingService {
    Flux<LabelDTO> findAll();
    Mono<Page<LabelDTO>> findAllByPage(Pageable pageable);
    Mono<LabelDTO> update(long id, LabelCommand.Update command);
    Mono<LabelDTO> upsert(LabelCommand.Create command);
    Mono<Void> delete(long id);
    Mono<LabelDTO> findById(long id);
}
