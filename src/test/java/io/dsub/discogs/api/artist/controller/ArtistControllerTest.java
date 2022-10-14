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
import reactor.test.StepVerifier;

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
        given(artistService.findAllByPage(pageable)).willReturn(Mono.just(expected));

        ResponseEntity<Mono<Page<ArtistDTO>>> response = artistController.getArtistsByPage(pageable);

        Mono<Page<ArtistDTO>> artistDtoPageMono = response.getBody();
        assertNotNull(artistDtoPageMono);

        Page<ArtistDTO> got = artistDtoPageMono.block();

        assertNotNull(got);
        assertEquals(expected, got);
        assertEquals(expected.getTotalElements(), 0);

        verify(artistService, times(1)).findAllByPage(pageable);
    }

    @Test
    void findByIDCallsService() {
        var artist = getRandomArtist();
        var expected = artist.toDTO();

        var artistID = artist.getId();
        assertNotNull(expected);
        assertNotNull(artistID);

        given(artistService.findById(artistID)).willReturn(Mono.just(artist.toDTO()));
        ResponseEntity<Mono<ArtistDTO>> response = artistController.getArtistById(artistID);

        Mono<ArtistDTO> responseDtoMono = response.getBody();
        assertNotNull(responseDtoMono);

        ArtistDTO got = responseDtoMono.block();
        assertNotNull(got);
        assertEquals(expected, got);

        verify(artistService, times(1)).findById(artistID);
    }

    @Test
    void createArtistCallsService() {
        Artist artist = getRandomArtist();
        ArtistCommand.Create command = getCreateCommandFrom(artist);
        ArtistDTO expected = artist.toDTO();
        given(artistService.upsert(command)).willReturn(Mono.just(artist.toDTO()));

        ResponseEntity<Mono<ArtistDTO>> responseEntity = artistController.createArtist(command);
        Mono<ArtistDTO> responseDTO = responseEntity.getBody();
        assertNotNull(responseDTO);
        ArtistDTO got = responseDTO.block();
        assertNotNull(got);
        assertEquals(expected, got);

        verify(artistService, times(1)).upsert(command);
    }

    @Test
    void updateArtistCallsService() {
        var id = TestUtil.getRandomIndexValue();
        var artist = getRandomArtist(id);

        var command = getUpdateCommandFrom(artist);
        var expected = artist.withMutableDataFrom(command).toDTO();
        var expectedMono = Mono.just(expected);

        given(artistService.update(id, command)).willReturn(expectedMono);

        var response = artistController.updateArtist(artist.getId(), command);
        assertNotNull(response);

        var responseBody = response.getBody();
        assertNotNull(responseBody);

        StepVerifier.create(responseBody)
                .expectNext(expected)
                .verifyComplete();

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

    private Artist getRandomArtist() {
        return getRandomArtist(TestUtil.getRandomIndexValue());
    }

    private Artist getRandomArtist(long id) {
        return Artist.builder()
                .id(id)
                .name(TestUtil.getRandomString())
                .realName(TestUtil.getRandomString())
                .profile(TestUtil.getRandomString())
                .dataQuality(TestUtil.getRandomString())
                .build();
    }

    private ArtistCommand.Create getCreateCommandFrom(Artist artist) {
        return ArtistCommand.Create.builder()
                .id(artist.getId())
                .name(artist.getName())
                .realName(artist.getRealName())
                .profile(artist.getProfile())
                .dataQuality(artist.getDataQuality())
                .build();
    }

    private ArtistCommand.Update getUpdateCommandFrom(Artist artist) {
        return ArtistCommand.Update.builder()
                .name(artist.getName())
                .realName(artist.getRealName())
                .profile(artist.getProfile())
                .dataQuality(artist.getDataQuality())
                .build();
    }
}
