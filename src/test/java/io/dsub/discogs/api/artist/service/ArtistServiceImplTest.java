package io.dsub.discogs.api.artist.service;

import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.repository.ArtistRepository;
import io.dsub.discogs.api.test.TestUtil;
import io.dsub.discogs.api.util.PageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

class ArtistServiceImplTest {
    @Mock
    ArtistRepository artistRepository;
    ArtistService artistService;

    @BeforeEach
    void setUp() {
        artistRepository = Mockito.mock(ArtistRepository.class);
        this.artistService = new ArtistServiceImpl(artistRepository);
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
    void updateOrInsert() {
        Map<Integer, Artist> artists = getArtistsByMap();
        Map<Integer, Artist> savedArtists = new ConcurrentHashMap<>();
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }


    private Map<Integer, Artist> getArtistsByMap() {
        return TestUtil.getRandomArtists(30)
                .toStream()
                .collect(Collectors.toConcurrentMap(Artist::getId, artist -> artist));
    }
}
