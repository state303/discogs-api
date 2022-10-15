package io.dsub.discogs.api.master.service;

import io.dsub.discogs.api.master.command.MasterCommand;
import io.dsub.discogs.api.master.dto.MasterDTO;
import io.dsub.discogs.api.master.model.Master;
import io.dsub.discogs.api.master.repository.MasterRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MasterServiceImplTest extends ConcurrentTest {

    @Mock
    MasterRepository repository;
    MasterServiceImpl masterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        masterService = new MasterServiceImpl(repository);
    }

    @Test
    void findAllReturnsAllItems() {
        List<Master> masters = IntStream.range(0, 10).mapToObj(i -> getRandomMaster()).toList();
        var expected = masters.stream().map(Master::toDTO).toList();
        given(repository.findAll()).willReturn(Flux.fromIterable(masters));
        StepVerifier.create(masterService.findAll())
                .expectNextSequence(expected)
                .verifyComplete();
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAllReturnsNothing() {
        given(repository.findAll()).willReturn(Flux.empty());
        StepVerifier.create(masterService.findAll()).verifyComplete();
        verify(repository, times(1)).findAll();
    }


    @Test
    void findAllByPage() {
        var masters = IntStream.range(0, 20).mapToObj(i -> getRandomMaster()).toList();
        var dtoList = masters.stream().map(Master::toDTO).toList();
        var pageable = PageRequest.of(1, 10);
        var captor = ArgumentCaptor.forClass(Pageable.class);

        Page<MasterDTO> page = new PageImpl<>(dtoList, pageable, masters.size());
        given(repository.findAllBy(captor.capture())).willReturn(Flux.fromIterable(masters));
        given(repository.count()).willReturn(Mono.just((long) 10));

        StepVerifier.create(masterService.findAllByPage(pageable))
                .expectNextMatches(p -> p.getTotalElements() == 20 &&
                        p.getTotalPages() == 2 &&
                        p.getNumberOfElements() == 10 &&
                        p.getNumber() == 1)
                .verifyComplete();
        verify(repository, times(1)).findAllBy(pageable);
        verify(repository, times(1)).count();

        assertThat(captor.getValue()).isEqualTo(pageable);
    }

    @Test
    void update() {
        MasterCommand.Update command = getRandomUpdateCommand();
        Master master = getRandomMaster();

        var updatedMaster = master.withMutableDataFrom(command);

        while (master.equals(updatedMaster)) {
            command = getRandomUpdateCommand();
            updatedMaster = master.withMutableDataFrom(command);
        }

        var captor = ArgumentCaptor.forClass(Master.class);
        assertNotNull(master.getId());

        given(repository.findById(master.getId())).willReturn(Mono.just(master));
        given(repository.save(captor.capture())).willReturn(Mono.just(updatedMaster));

        StepVerifier.create(masterService.update(master.getId(), command))
                .expectNext(updatedMaster.toDTO())
                .verifyComplete();

        verify(repository, times(1)).findById(master.getId());
        verify(repository, times(1)).save(any());

        assertThat(captor.getValue()).isEqualTo(updatedMaster);
    }

    @Test
    void upsert() {
        var command = getRandomCreateCommand();
        var master = Master.fromCreateCommand(command);
        var createdAt = LocalDateTime.now();
        var lastModifiedAt = LocalDateTime.now();

        var captor = ArgumentCaptor.forClass(Master.class);
        var updated = master.withCreatedAt(createdAt).withLastModifiedAt(lastModifiedAt);

        given(repository.saveOrUpdate(captor.capture()))
                .willReturn(Mono.just(master.withCreatedAt(createdAt).withLastModifiedAt(lastModifiedAt)));

        StepVerifier.create(masterService.upsert(command))
                .expectNext(updated.toDTO())
                .verifyComplete();

        verify(repository, times(1)).saveOrUpdate(master);
        assertThat(captor.getValue()).isEqualTo(master);
    }

    @Test
    void delete() {
        var captor = ArgumentCaptor.forClass(Long.class);
        given(repository.deleteById(captor.capture())).willReturn(Mono.empty());
        StepVerifier.create(masterService.delete(1L)).verifyComplete();
        verify(repository, times(1)).deleteById(1L);
        assertThat(captor.getValue()).isEqualTo(1L);
    }

    @Test
    void findById() {
        var master = getRandomMaster();
        var captor = ArgumentCaptor.forClass(Long.class);
        given(repository.findById(captor.capture())).willReturn(Mono.just(master));
        StepVerifier.create(masterService.findById(1L))
                .expectNext(master.toDTO())
                .verifyComplete();
        verify(repository, times(1)).findById(1L);
        assertThat(captor.getValue()).isEqualTo(1L);
    }

    private MasterCommand.Create getRandomCreateCommand() {
        return TestUtil.getInstanceOf(MasterCommand.Create.class);
    }

    private MasterCommand.Update getRandomUpdateCommand() {
        return TestUtil.getInstanceOf(MasterCommand.Update.class);
    }

    private Master getRandomMaster() {
        return TestUtil.getInstanceOf(Master.class);
    }
}