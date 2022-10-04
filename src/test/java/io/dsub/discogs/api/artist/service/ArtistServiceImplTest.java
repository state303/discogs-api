package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.TestUtil;
import io.dsub.discogs.api.util.PageUtil;
import io.dsub.discogs.api.validator.ReactiveValidator;
import io.dsub.discogs.api.validator.ReactiveValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.dsub.discogs.api.artist.command.ArtistCommand.CreateArtistCommand;
import static io.dsub.discogs.api.artist.command.ArtistCommand.UpdateArtistCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ArtistServiceImplTest extends ConcurrentTest {
    @Mock
    ArtistRepository artistRepository;

    ReactiveValidator validator;

    ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = Mockito.spy(new ReactiveValidatorImpl(TestUtil.getNoOpValidator()));
        this.artistService = new ArtistServiceImpl(artistRepository, validator);
    }

    @Test
    void getArtistsByPageAndSizeWithNoArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        given(artistRepository.findAllByNameNotNullOrderByNameAscIdAsc(pageRequest)).willReturn(Flux.empty());
        given(artistRepository.count()).willReturn(Mono.just((long) 0));

        Page<ArtistDTO> page = artistService.getArtistsByPageAndSize(pageRequest).block();

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());
    }

    @Test
    void getArtistsByPageAndSizeWithSomeArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        List<Artist> artists = TestUtil.getRandomArtists(5).collectList().block();

        assertNotNull(artists);

        given(artistRepository.findAllByNameNotNullOrderByNameAscIdAsc(pageRequest)).willReturn(Flux.fromIterable(artists));
        given(artistRepository.count()).willReturn(Mono.just((long) 5));


        Iterator<Artist> artistIterator = artists.iterator();
        Page<ArtistDTO> page = artistService.getArtistsByPageAndSize(pageRequest).block();

        assertNotNull(page);
        assertEquals(5, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        page.forEach(got -> {
            ArtistDTO expected = artistIterator.next().toDTO().block();
            assertEquals(expected, got);
        });
    }

    @Test
    void getArtistsByPageAndSizeWithNullPageable() {
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

        given(artistRepository.findAllByNameNotNullOrderByNameAscIdAsc(captor.capture())).willReturn(Flux.empty());
        given(artistRepository.count()).willReturn(Mono.just((long) 0));

        Page<ArtistDTO> page = artistService.getArtistsByPageAndSize(null).block();

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());

        Pageable got = captor.getValue();
        Pageable expected = PageUtil.getOrDefaultPageable(null);
        assertEquals(expected, got);
    }

    @Test
    void insertOrUpdateInsertsAllItems() {
        List<Artist> artists = TestUtil.getRandomArtists(5).collectList().block();
        assertNotNull(artists);

        AtomicInteger found = new AtomicInteger(0);

        artists.forEach(artist -> {
            CreateArtistCommand createCommand = TestUtil.getCreateCommandFrom(artist);
            given(artistRepository.insertOrUpdate(createCommand)).willReturn(Mono.just(artist));
            ArtistDTO expected = artist.toDTO().block();
            ArtistDTO got = artistService.updateOrInsert(createCommand).block();
            assertEquals(expected, got);
            assertNotNull(got);
            assertEquals(createCommand.getId(), artist.getId());
            assertEquals(createCommand.getName(), artist.getName());
            assertEquals(createCommand.getRealName(), artist.getRealName());
            assertEquals(createCommand.getProfile(), artist.getProfile());
            assertEquals(createCommand.getDataQuality(), artist.getDataQuality());
            found.addAndGet(1);
        });

        assertEquals(5, found.get());
    }

    @Test
    void insertOrUpdateThrowsIfMissingName() {
        CreateArtistCommand command =
                new CreateArtistCommand(3, null, null, null, null);

        String error = "test error message";

        given(validator.validate(command))
                .willReturn(Mono.error(new RuntimeException(error)));

        ArgumentCaptor<CreateArtistCommand> argumentCaptor =
                ArgumentCaptor.forClass(CreateArtistCommand.class);

        StepVerifier.create(artistService.updateOrInsert(command))
                .expectErrorMessage(error)
                .verify();

        verifyNoInteractions(artistRepository);
    }

    @Test
    void insertOrUpdateWillNotCallArtistRepositoryWithError() {
        String error = "test error message";

        given(validator.validate(any()))
                .willReturn(Mono.error(new RuntimeException(error)));

        ArgumentCaptor<CreateArtistCommand> captor =
                ArgumentCaptor.forClass(CreateArtistCommand.class);

        StepVerifier.create(artistService.updateOrInsert(null))
                .expectErrorMessage(error)
                .verify();

        verifyNoInteractions(artistRepository);
    }

    @Test
    void updateCallsRepositoryUpdate() {
        int id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);
        ArtistDTO dto = artist.toDTO().block();
        assertNotNull(dto);
        UpdateArtistCommand cmd = new UpdateArtistCommand("a", "b", "c", "d");

        given(artistRepository.findById(id))
                .willReturn(Mono.just(artist));
        given(artistRepository.save(any()))
                .willReturn(Mono.just(artist.withMutableDataFrom(cmd)));

        Mono<ArtistDTO> resultDTO = artistService.update(id, cmd);
        ArtistDTO got = resultDTO.block();

        assertNotNull(got);
        assertEquals("a", got.name());
        assertEquals("b", got.realName());
        assertEquals("c", got.profile());
        assertEquals("d", got.dataQuality());
    }

    @Test
    void deleteCallsRepositoryWithSameID() {
        int id = TestUtil.getRandomIndexValue();
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        given(artistRepository.deleteById(idCaptor.capture())).willReturn(Mono.empty());
        StepVerifier.create(artistService.delete(id)).verifyComplete();
        assertEquals(id, idCaptor.getValue());
        verify(artistRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void deleteNotCallsRepositoryDeleteWithZeroOrNegative(int value) {
        given(artistRepository.deleteById(any(Integer.class))).willReturn(Mono.empty());
        StepVerifier.create(artistService.delete(value)).verifyComplete();
        verifyNoInteractions(artistRepository);
    }

    @Test
    void findByIdReturnsValidItem() {
        int id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        given(artistRepository.findById(captor.capture())).willReturn(Mono.just(artist));

        ArtistDTO got = artistService.findById(id).block();
        ArtistDTO expected = artist.toDTO().block();
        assertNotNull(got);
        assertEquals(expected, got);

        assertEquals(artist.getId(), captor.getValue());
    }


    private Map<Integer, Artist> getArtistsByMap() {
        return TestUtil.getRandomArtists(30)
                .toStream()
                .collect(Collectors.toConcurrentMap(Artist::getId, artist -> artist));
    }
}
