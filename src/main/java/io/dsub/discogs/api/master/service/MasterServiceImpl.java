package io.dsub.discogs.api.master.service;

import io.dsub.discogs.api.master.command.MasterCommand;
import io.dsub.discogs.api.master.dto.MasterDTO;
import io.dsub.discogs.api.master.model.Master;
import io.dsub.discogs.api.master.repository.MasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final Function<Master, Mono<MasterDTO>> toDTO = master -> Mono.just(master.toDTO());

    @Override
    public Flux<MasterDTO> findAll() {
        return masterRepository.findAll().flatMap(toDTO);
    }

    @Override
    public Mono<Page<MasterDTO>> findAllByPage(Pageable pageable) {
        Flux<MasterDTO> dtoFlux = masterRepository.findAllBy(pageable).flatMap(toDTO);
        return getPagedResult(masterRepository.count(), pageable, dtoFlux);
    }

    @Override
    public Mono<MasterDTO> update(long id, MasterCommand.Update command) {
        return masterRepository.findById(id)
                .flatMap(master -> Mono.just(master.withMutableDataFrom(command)))
                .flatMap(masterRepository::save)
                .flatMap(toDTO);
    }

    @Override
    public Mono<MasterDTO> upsert(MasterCommand.Create command) {
        return masterRepository.saveOrUpdate(Master.fromCreateCommand(command)).flatMap(toDTO);
    }

    @Override
    public Mono<Void> delete(long id) {
        return masterRepository.deleteById(id);
    }

    @Override
    public Mono<MasterDTO> findById(long id) {
        return masterRepository.findById(id).flatMap(toDTO);
    }
}
