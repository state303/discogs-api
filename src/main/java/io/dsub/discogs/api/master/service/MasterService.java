package io.dsub.discogs.api.master.service;

import io.dsub.discogs.api.core.service.PagingService;
import io.dsub.discogs.api.master.command.MasterCommand;
import io.dsub.discogs.api.master.dto.MasterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MasterService extends PagingService {
    Flux<MasterDTO> findAll();
    Mono<Page<MasterDTO>> findAllByPage(Pageable pageable);
    Mono<MasterDTO> update(long id, MasterCommand.Update command);
    Mono<MasterDTO> upsert(MasterCommand.Create command);
    Mono<Void> delete(long id);
    Mono<MasterDTO> findById(long id);
}
