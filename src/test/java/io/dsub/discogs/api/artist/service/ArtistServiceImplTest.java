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

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import static io.dsub.discogs.api.artist.command.ArtistCommand.Update;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ArtistServiceImplTest extends ConcurrentTest {
    @Mock
    ArtistRepository artistRepository;

    ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.artistService = new ArtistServiceImpl(artistRepository);
    }

    @Test
    void getArtistsCallsRepository() {
        Flux<Artist> emptyArtistFlux = Flux.empty();
        given(artistRepository.findAll()).willReturn(emptyArtistFlux);
        StepVerifier.create(artistService.findAll()).verifyComplete();
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void getArtistsReturnsAllItems() {
        var artists = IntStream.range(0, 3).mapToObj(i -> TestUtil.getInstanceOf(Artist.class)).toList();
        var iter = artists.stream().map(Artist::toDTO).toList().iterator();
        given(artistRepository.findAll()).willReturn(Flux.fromIterable(artists));
        StepVerifier.create(artistService.findAll())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .verifyComplete();
        verify(artistRepository, times(1)).findAll();
    }

    @Test
    void getArtistsByPageAndSizeWithNoArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        given(artistRepository.findAllBy(pageRequest)).willReturn(Flux.empty());
        given(artistRepository.count()).willReturn(Mono.just((long) 0));

        Page<ArtistDTO> page = artistService.findAllByPage(pageRequest).block();

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());
    }

    @Test
    void getArtistsByPageAndSizeWithSomeArtists() {
        Pageable pageRequest = PageRequest.of(0, 5);
        var artists = IntStream.range(0, 5).mapToObj(i -> TestUtil.getInstanceOf(Artist.class)).toList();
        given(artistRepository.findAllBy(pageRequest)).willReturn(Flux.fromIterable(artists));
        given(artistRepository.count()).willReturn(Mono.just((long) 5));

        Iterator<Artist> artistIterator = artists.iterator();
        Page<ArtistDTO> page = artistService.findAllByPage(pageRequest).block();

        assertNotNull(page);
        assertEquals(5, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        page.forEach(got -> {
            ArtistDTO expected = artistIterator.next().toDTO();
            assertEquals(expected, got);
        });
    }
    @Test
    void updateCallsRepositoryUpdate() {
        var artist = TestUtil.getInstanceOf(Artist.class);
        var id = artist.getId();
        assertNotNull(id);

        Update cmd = new Update("a", "b", "c", "d");
        var captor = ArgumentCaptor.forClass(Artist.class);
        var expectedReceived = artist.withMutableDataFrom(cmd);

        given(artistRepository.findById(id))
                .willReturn(Mono.just(artist));
        given(artistRepository.save(captor.capture()))
                .willReturn(Mono.just(artist.withMutableDataFrom(cmd)));

        StepVerifier.create(artistService.update(id, cmd))
                .expectNextMatches(dto -> dto != null &&
                        dto.getName().equals("a") &&
                        dto.getRealName().equals("b") &&
                        dto.getProfile().equals("c") &&
                        dto.getDataQuality().equals("d"))
                .verifyComplete();

        var received = captor.getValue();
        assertThat(received).isEqualTo(expectedReceived);

        verify(artistRepository, times(1)).findById(id);
        verify(artistRepository, times(1)).save(expectedReceived);
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
        Artist artist = TestUtil.getInstanceOf(Artist.class);
        assertNotNull(artist);
        Long id = artist.getId();
        assertNotNull(id);

        var captor = ArgumentCaptor.forClass(Long.class);
        given(artistRepository.findById(captor.capture())).willReturn(Mono.just(artist));

        ArtistDTO got = artistService.findById(id).block();
        ArtistDTO expected = artist.toDTO();
        assertNotNull(got);
        assertEquals(expected, got);

        assertEquals(artist.getId(), captor.getValue());
    }
}
