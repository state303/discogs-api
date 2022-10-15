package io.dsub.discogs.api.release.service;

import io.dsub.discogs.api.core.exception.ItemNotFoundException;
import io.dsub.discogs.api.release.command.ReleaseCommand;
import io.dsub.discogs.api.release.dto.ReleaseDTO;
import io.dsub.discogs.api.release.model.Release;
import io.dsub.discogs.api.release.repository.ReleaseRepository;
import io.dsub.discogs.api.test.util.TestUtil;
import io.dsub.discogs.api.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ReleaseServiceImplTest {
    @Mock
    ReleaseRepository repository;

    ReleaseServiceImpl releaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        releaseService = new ReleaseServiceImpl(repository);
    }

    @Test
    void findAllReturnsAllItems() {
        List<Release> releases = IntStream.range(0, 10).mapToObj(i -> getRandomRelease()).toList();
        List<ReleaseDTO> expectedDTOs = releases.stream().map(Release::toDTO).toList();
        Flux<Release> releasesFlux = Flux.fromIterable(releases);
        given(repository.findAll()).willReturn(releasesFlux);

        StepVerifier.create(releaseService.findAll())
                .expectNextSequence(expectedDTOs)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void findAllByPage() {
        List<Release> releases = IntStream.range(0, 10).mapToObj(i -> getRandomRelease()).toList();
        int pageSize = releases.size() / 2;
        Pageable pageable = PageRequest.of(0, pageSize);
        given(repository.findAll(pageable.getSort())).willReturn(Flux.fromIterable(releases));
        given(repository.count()).willReturn(Mono.just(10L));

        StepVerifier.create(releaseService.findAllByPage(pageable))
                .expectNextMatches(page -> page.hasContent() &&
                        page.getTotalPages() == 2 &&
                        page.getTotalElements() == releases.size() &&
                        page.getNumberOfElements() == pageSize &&
                        page.getNumber() == 0)
                .verifyComplete();

        verify(repository, times(1)).findAll(pageable.getSort());
    }

    @Test
    void findByIdMustReturnItem() {
        var release = getRandomRelease();
        assertNotNull(release.getId());
        given(repository.findById(release.getId())).willReturn(Mono.just(release));
        StepVerifier.create(releaseService.findById(release.getId()))
                .expectNext(release.toDTO())
                .verifyComplete();
        verify(repository, times(1)).findById(release.getId());
    }

    @Test
    void upsertCallsRepositorySaveOrUpdate() {
        var release = getRandomRelease();
        var captor = ArgumentCaptor.forClass(Release.class);
        given(repository.saveOrUpdate(captor.capture())).willReturn(Mono.just(release));
        StepVerifier.create(releaseService.upsert(getCreateCommandFrom(release)))
                .expectNext(release.toDTO())
                .verifyComplete();
        verify(repository, times(1)).saveOrUpdate(release);
        assertThat(captor.getValue()).isEqualTo(release);
    }

    @Test
    void updateCallsRepositoryUpdate() {
        var release = getRandomRelease();
        var idCaptor = ArgumentCaptor.forClass(Long.class);
        var releaseCaptor = ArgumentCaptor.forClass(Release.class);
        assertNotNull(release.getId());
        given(repository.findById(idCaptor.capture())).willReturn(Mono.just(release));
        given(repository.save(releaseCaptor.capture())).willReturn(Mono.just(release));
        StepVerifier.create(releaseService.update(release.getId(), getUpdateCommandFrom(release)))
                .expectNext(release.toDTO())
                .verifyComplete();

        verify(repository, times(1)).findById(release.getId());
        verify(repository, times(1)).save(release);
        assertThat(idCaptor.getValue()).isEqualTo(release.getId());
        assertThat(releaseCaptor.getValue()).isEqualTo(release);
    }

    @Test
    void updateNotCallRepositoryUpdateWhenEmpty() {
        var release = getRandomRelease();
        var captor = ArgumentCaptor.forClass(Long.class);
        assertNotNull(release.getId());
        given(repository.findById(captor.capture())).willReturn(Mono.empty());
        StepVerifier.create(releaseService.update(release.getId(), getUpdateCommandFrom(release))).verifyComplete();

        verify(repository, times(1)).findById(release.getId());
        verify(repository, never()).save(any());
    }

    @Test
    void delete() {
        long id = getRandomInt(1, 10);
        given(repository.deleteById(id)).willReturn(Mono.empty());
        StepVerifier.create(releaseService.delete(id)).verifyComplete();
        verify(repository, times(1)).deleteById(id);
    }

    private Release getRandomRelease() {
        var y = getRandomInt(1950, 2020);
        var m = getRandomInt(1, 12);
        var d = getRandomInt(1, 28);
        LocalDate date = LocalDate.of(y, m, d);
        return TestUtil.getInstanceOf(Release.class)
                .withReleaseDate(date)
                .withListedReleaseDate(date.format(DateTimeFormatter.BASIC_ISO_DATE))
                .withHasValidDay(true)
                .withHasValidYear(true)
                .withHasValidMonth(true);
    }

    private ReleaseCommand.Update getUpdateCommandFrom(Release release) {
        return ReleaseCommand.Update.builder()
                .releaseDate(release.getListedReleaseDate())
                .dataQuality(release.getDataQuality())
                .status(release.getStatus())
                .title(release.getTitle())
                .country(release.getCountry())
                .notes(release.getNotes())
                .isMaster(release.getIsMaster())
                .masterId(release.getMasterId())
                .build();
    }

    private ReleaseCommand.Create getCreateCommandFrom(Release release) {
        return ReleaseCommand.Create.builder()
                .id(release.getId())
                .releaseDate(release.getListedReleaseDate())
                .dataQuality(release.getDataQuality())
                .status(release.getStatus())
                .title(release.getTitle())
                .country(release.getCountry())
                .notes(release.getNotes())
                .isMaster(release.getIsMaster())
                .masterId(release.getMasterId())
                .build();
    }

    private int getRandomInt(int min, int max) {
        return TestUtil.RANDOM.nextInt(min, max + 1);
    }
}