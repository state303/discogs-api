package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import io.dsub.discogs.api.validator.Validator;
import io.dsub.discogs.api.validator.ValidatorImpl;
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

import static io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import static io.dsub.discogs.api.artist.command.ArtistCommand.Update;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ArtistServiceImplTest extends ConcurrentTest {
    @Mock
    ArtistRepository artistRepository;

    Validator validator;

    ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = Mockito.spy(new ValidatorImpl(TestUtil.getNoOpValidator()));
        this.artistService = new ArtistServiceImpl(artistRepository, validator);
    }

    @Test
    void getArtistsCallsRepository() {
        Flux<Artist> emptyArtistFlux = Flux.empty();
        given(artistRepository.findAll()).willReturn(emptyArtistFlux);
        StepVerifier.create(artistService.getArtists()).verifyComplete();
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void getArtistsReturnsAllItems() {
        List<Artist> artists = TestUtil.getRandomArtists(3).collectList().block();
        assertNotNull(artists);
        Iterator<ArtistDTO> iter = artists.stream().map(Artist::toDTO).toList().iterator();
        given(artistRepository.findAll()).willReturn(Flux.fromIterable(artists));
        StepVerifier.create(artistService.getArtists())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .verifyComplete();
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void getArtistsByPageAndSizeWithNoArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        given(artistRepository.findAll(pageRequest.getSort())).willReturn(Flux.empty());
        given(artistRepository.count()).willReturn(Mono.just((long) 0));

        Page<ArtistDTO> page = artistService.getArtists(pageRequest).block();

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());
    }

    @Test
    void getArtistsByPageAndSizeWithSomeArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        List<Artist> artists = TestUtil.getRandomArtists(5).collectList().block();

        assertNotNull(artists);

        given(artistRepository.findAll(pageRequest.getSort())).willReturn(Flux.fromIterable(artists));
        given(artistRepository.count()).willReturn(Mono.just((long) 5));


        Iterator<Artist> artistIterator = artists.iterator();
        Page<ArtistDTO> page = artistService.getArtists(pageRequest).block();

        assertNotNull(page);
        assertEquals(5, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        page.forEach(got -> {
            ArtistDTO expected = artistIterator.next().toDTO();
            assertEquals(expected, got);
        });
    }

    @Test
    void insertOrUpdateInsertsAllItems() {
        List<Artist> artists = TestUtil.getRandomArtists(5).collectList().block();
        assertNotNull(artists);

        AtomicInteger found = new AtomicInteger(0);

        artists.forEach(artist -> {
            Create createCommand = TestUtil.getCreateCommandFrom(artist);
            given(artistRepository.saveOrUpdate(createCommand)).willReturn(Mono.just(artist));
            ArtistDTO expected = artist.toDTO();
            ArtistDTO got = artistService.upsert(createCommand).block();
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
        Create command =
                new Create(3L, null, null, null, null);

        String error = "test error message";

        given(validator.validate(command))
                .willReturn(Mono.error(new RuntimeException(error)));

        ArgumentCaptor<Create> argumentCaptor =
                ArgumentCaptor.forClass(Create.class);

        StepVerifier.create(artistService.upsert(command))
                .expectErrorMessage(error)
                .verify();

        verifyNoInteractions(artistRepository);
    }

    @Test
    void insertOrUpdateWillNotCallArtistRepositoryWithError() {
        String error = "test error message";

        given(validator.validate(any()))
                .willReturn(Mono.error(new RuntimeException(error)));

        ArgumentCaptor<Create> captor =
                ArgumentCaptor.forClass(Create.class);

        StepVerifier.create(artistService.upsert(null))
                .expectErrorMessage(error)
                .verify();

        verifyNoInteractions(artistRepository);
    }

    @Test
    void updateCallsRepositoryUpdate() {
        long id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);
        Update cmd = new Update("a", "b", "c", "d");

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
        long id = TestUtil.getRandomIndexValue();
        var idCaptor = ArgumentCaptor.forClass(Long.class);
        given(artistRepository.deleteById(idCaptor.capture())).willReturn(Mono.empty());
        StepVerifier.create(artistService.delete(id)).verifyComplete();
        assertEquals(id, idCaptor.getValue());
        verify(artistRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void deleteNotCallsRepositoryDeleteWithZeroOrNegative(int value) {
        given(artistRepository.deleteById(any(Long.class))).willReturn(Mono.empty());
        StepVerifier.create(artistService.delete(value)).verifyComplete();
        verifyNoInteractions(artistRepository);
    }

    @Test
    void findByIdReturnsValidItem() {
        long id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);

        var captor = ArgumentCaptor.forClass(Long.class);
        given(artistRepository.findById(captor.capture())).willReturn(Mono.just(artist));

        ArtistDTO got = artistService.findById(id).block();
        ArtistDTO expected = artist.toDTO();
        assertNotNull(got);
        assertEquals(expected, got);

        assertEquals(artist.getId(), captor.getValue());
    }

    private Map<Long, Artist> getArtistsByMap() {
        return TestUtil.getRandomArtists(30)
                .toStream()
                .collect(Collectors.toConcurrentMap(Artist::getId, artist -> artist));
    }
}
