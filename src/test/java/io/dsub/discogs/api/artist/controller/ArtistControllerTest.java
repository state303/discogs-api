package io.dsub.discogs.api.artist.controller;

import io.dsub.discogs.api.artist.command.ArtistCommand;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.artist.service.ArtistService;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ArtistControllerTest extends ConcurrentTest {

    @Mock
    ArtistService artistService;

    ArtistController artistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        artistController = new ArtistController(artistService);
    }

    @Test
    void getArtistsByPageReturnsDelegatedResult() {
        Pageable pageable = PageRequest.of(3, 10);
        Page<ArtistDTO> expected = new PageImpl<>(List.of());
        given(artistService.getArtists(pageable)).willReturn(Mono.just(expected));

        ResponseEntity<Mono<Page<ArtistDTO>>> response = artistController.getArtistsByPage(pageable);

        Mono<Page<ArtistDTO>> artistDtoPageMono = response.getBody();
        assertNotNull(artistDtoPageMono);

        Page<ArtistDTO> got = artistDtoPageMono.block();

        assertNotNull(got);
        assertEquals(expected, got);
        assertEquals(expected.getTotalElements(), 0);

        verify(artistService, times(1)).getArtists(pageable);
    }

    @Test
    void getArtistByIdCallsService() {
        long id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);
        ArtistDTO expected = artist.toDTO();
        assertNotNull(expected);

        given(artistService.findById(id)).willReturn(Mono.just(artist.toDTO()));
        ResponseEntity<Mono<ArtistDTO>> response = artistController.getArtistById(id);

        Mono<ArtistDTO> responseDtoMono = response.getBody();
        assertNotNull(responseDtoMono);

        ArtistDTO got = responseDtoMono.block();
        assertNotNull(got);
        assertEquals(expected, got);

        verify(artistService, times(1)).findById(id);
    }

    @Test
    void createArtistCallsService() {
        Artist artist = TestUtil.getRandomArtist();
        ArtistCommand.Create command = TestUtil.getCreateCommandFrom(artist);
        ArtistDTO expected = artist.toDTO();
        given(artistService.saveOrUpdate(command)).willReturn(Mono.just(artist.toDTO()));

        ResponseEntity<Mono<ArtistDTO>> responseEntity = artistController.createArtist(command);
        Mono<ArtistDTO> responseDTO = responseEntity.getBody();
        assertNotNull(responseDTO);
        ArtistDTO got = responseDTO.block();
        assertNotNull(got);
        assertEquals(expected, got);

        verify(artistService, times(1)).saveOrUpdate(command);
    }

    @Test
    void updateArtistCallsService() {
        int id = TestUtil.getRandomIndexValue();
        Artist artist = TestUtil.getRandomArtist(id);
        Artist other = TestUtil.getRandomArtist(id);

        ArtistCommand.Update command = TestUtil.getUpdateCommandFrom(artist);
        Mono<ArtistDTO> expectedMono = Mono.just(artist.withMutableDataFrom(command).toDTO());
        given(artistService.update(id, command)).willReturn(expectedMono);

        ResponseEntity<Mono<ArtistDTO>> response = artistController.updateArtist(artist.getId(), command);
        Mono<ArtistDTO> responseDTO = response.getBody();
        assertNotNull(responseDTO);
        ArtistDTO got = responseDTO.block();
        ArtistDTO expected = expectedMono.block();

        assertNotNull(got);
        assertNotNull(expected);
        assertEquals(id, got.id());
        assertEquals(expected.name(), got.name());
        assertEquals(expected.profile(), got.profile());
        assertEquals(expected.realName(), got.realName());
        assertEquals(expected.dataQuality(), got.dataQuality());

        verify(artistService, times(1)).update(id, command);
    }

    @Test
    void deleteArtistByIdCallsService() {
        long id = TestUtil.getRandomIndexValue();
        Mono<Void> voidMono = Mono.empty();

        given(artistService.delete(id)).willReturn(voidMono);
        ResponseEntity<Mono<Void>> entity = artistController.deleteArtistById(id);

        Mono<Void> gotMono = entity.getBody();
        assertNotNull(gotMono);
        assertEquals(voidMono, gotMono);

        verify(artistService, times(1)).delete(id);
    }
}
