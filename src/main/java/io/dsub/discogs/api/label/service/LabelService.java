package io.dsub.discogs.api.label.service;

import io.dsub.discogs.api.label.command.LabelCommand;
import io.dsub.discogs.api.label.dto.LabelDTO;
import io.dsub.discogs.api.service.PagingService;
import io.dsub.discogs.api.service.ValidatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LabelService extends ValidatingService, PagingService {
    Flux<LabelDTO> getLabels();
    Mono<Page<LabelDTO>> getLabels(Pageable pageable);
    Mono<LabelDTO> updateLabel(LabelCommand.Update command);
    Mono<LabelDTO> saveOrUpdate(LabelCommand.Create command);
    Mono<Void> deleteLabel(Integer id);
}
