package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.artist.model.Artist;
import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.genre.model.Genre;
import io.dsub.discogs.api.genre.repository.GenreRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
import io.dsub.discogs.api.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class GenreServiceImplTest extends ConcurrentTest {

    @Mock
    GenreRepository genreRepository;

    GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        genreService = new GenreServiceImpl(genreRepository);
    }

    @Test
    void findAllReturnsAllItems() {
        List<Genre> genres = getGenres(3);
        Iterator<GenreDTO> iter = genres.stream().map(Genre::toDTO).iterator();
        given(genreRepository.findAll()).willReturn(Flux.fromIterable(genres));

        StepVerifier.create(genreService.findAll())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .verifyComplete();

        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void findAllReturnsEmptyFlux() {
        given(genreRepository.findAll()).willReturn(Flux.empty());
        StepVerifier.create(genreService.findAll()).verifyComplete();
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void findAllReturnsWithPage() {
        int size = 10;

        Flux<Genre> genreFlux = Flux.fromIterable(getGenres(size));
        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        Pageable pageable = PageRequest.of(0, size);

        given(genreRepository.findAll(captor.capture())).willReturn(genreFlux);
        given(genreRepository.count()).willReturn(Mono.just((long) size));

        Mono<Page<GenreDTO>> result = genreService.findAllByPage(pageable);
        assertNotNull(result);

        Page<GenreDTO> page = result.block();
        assertNotNull(page);

        assertEquals(size, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        verify(genreRepository, times(1)).findAll(pageable.getSort());
        assertEquals(pageable.getSort(), captor.getValue());
    }

    @Test
    void findAllReturnsEmptyPage() {
        Flux<Genre> emptyGenreFlux = Flux.empty();
        final Pageable pageable = PageRequest.of(0, 1);

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        given(genreRepository.findAll(captor.capture())).willReturn(emptyGenreFlux);
        given(genreRepository.count()).willReturn(Mono.just((long) 0));

        Mono<Page<GenreDTO>> result = genreService.findAllByPage(pageable);
        assertNotNull(result);
        final Page<GenreDTO> page = result.block();
        assertNotNull(page);

        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());
        verify(genreRepository, times(1)).findAll(pageable.getSort());
        assertEquals(pageable.getSort(), captor.getValue());
    }

    @Test
    void saveCallsRepositorySave() {
        final var genre = getGenre();
        final var dto = genre.toDTO();

        assertNotNull(dto);
        final var captor = ArgumentCaptor.forClass(Genre.class);

        given(genreRepository.saveOrUpdate(captor.capture()))
                .willReturn(Mono.just(genre));

        StepVerifier.create(genreService.save(genre.getName()))
                .expectNext(dto)
                .verifyComplete();

        verify(genreRepository, times(1)).saveOrUpdate(any());
        assertEquals(genre.getName(), captor.getValue().getName());
    }
    @Test
    void deleteCallsRepositorySave() {
        final Genre genre = getGenre();
        final GenreDTO dto = genre.toDTO();
        assertNotNull(dto);

        given(genreRepository.deleteById(genre.getName())).willReturn(Mono.empty());

        StepVerifier.create(genreService.delete(genre.getName())).verifyComplete();
        verify(genreRepository, times(1)).deleteById(genre.getName());
    }


    private List<Genre> getGenres(int size) {
        return IntStream.range(0, size).mapToObj(i -> getGenre()).toList();
    }
    private Genre getGenre() {
        return Genre.builder().name(TestUtil.getRandomString()).build();
    }
}
