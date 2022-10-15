package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.style.dto.StyleDTO;
import io.dsub.discogs.api.style.model.Style;
import io.dsub.discogs.api.style.repository.StyleRepository;
import io.dsub.discogs.api.test.ConcurrentTest;
import io.dsub.discogs.api.test.util.TestUtil;
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

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StyleServiceImplTest extends ConcurrentTest {

    @Mock
    StyleRepository styleRepository;
    StyleServiceImpl styleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        styleService = new StyleServiceImpl(styleRepository);
    }

    @Test
    void findAllReturnsAllItem() {
        List<Style> styles = getStyles(5);
        assertEquals(5, styles.size());
        Iterator<StyleDTO> iter = styles.stream().map(Style::toDTO).iterator();
        given(styleRepository.findAll()).willReturn(Flux.fromIterable(styles));

        StepVerifier.create(styleService.findAll())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .expectNext(iter.next())
                .verifyComplete();

        verify(styleRepository, times(1)).findAll();
    }

    @Test
    void findAllReturnsEmptyFlux() {
        given(styleRepository.findAll()).willReturn(Flux.empty());
        StepVerifier.create(styleService.findAll()).verifyComplete();
        verify(styleRepository, times(1)).findAll();
    }

    @Test
    void findAllWithPage() {
        int size = 10;

        Flux<Style> styleFlux = Flux.fromIterable(getStyles(size));
        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        Pageable pageable = PageRequest.of(0, size);

        given(styleRepository.findAll(captor.capture())).willReturn(styleFlux);
        given(styleRepository.count()).willReturn(Mono.just((long) size));

        Mono<Page<StyleDTO>> result = styleService.findAll(pageable);
        assertNotNull(result);
        Page<StyleDTO> page = result.block();
        assertNotNull(page);

        assertEquals(size, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        verify(styleRepository, times(1)).findAll(pageable.getSort());
        assertEquals(pageable.getSort(), captor.getValue());
    }

    @Test
    void findAllReturnsEmptyPage() {
        Flux<Style> emptyStyleFlux = Flux.empty();
        final Pageable pageable = PageRequest.of(0, 1);

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        given(styleRepository.findAll(captor.capture())).willReturn(emptyStyleFlux);
        given(styleRepository.count()).willReturn(Mono.just((long) 0));

        final Mono<Page<StyleDTO>> result = styleService.findAll(pageable);
        assertNotNull(result);
        final Page<StyleDTO> page = result.block();
        assertNotNull(page);

        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getTotalElements());
        verify(styleRepository, times(1)).findAll(pageable.getSort());
        assertEquals(pageable.getSort(), captor.getValue());
    }

    @Test
    void saveCallsRepositorySave() {
        var style = getStyle();
        var dto = style.toDTO();
        var captor = ArgumentCaptor.forClass(String.class);

        given(styleRepository.saveOrUpdate(captor.capture())).willReturn(Mono.just(style));

        StepVerifier.create(styleService.upsert(style.getName()))
                .expectNext(dto)
                .verifyComplete();

        verify(styleRepository, times(1)).saveOrUpdate(style.getName());
        assertThat(captor.getValue()).isEqualTo(style.getName());
    }

    @Test
    void deleteCallsRepositoryDelete() {
        final Style style = getStyle();

        given(styleRepository.deleteById(style.getName())).willReturn(Mono.empty());
        StepVerifier.create(styleService.delete(style.getName())).verifyComplete();

        verify(styleRepository, times(1)).deleteById(style.getName());
    }

    private List<Style> getStyles(int size) {
        return IntStream.range(0, size).mapToObj(i -> getStyle()).toList();
    }

    private Style getStyle() {
        return Style.builder().name(TestUtil.getRandomString()).build();
    }
}
