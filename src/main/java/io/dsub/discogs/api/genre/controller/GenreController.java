package io.dsub.discogs.api.genre.controller;

import io.dsub.discogs.api.genre.dto.GenreDTO;
import io.dsub.discogs.api.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping(path = "/stream")
    public ResponseEntity<Flux<GenreDTO>> streamGenres() {
       return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping
    public ResponseEntity<Mono<Page<GenreDTO>>> genres(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(genreService.findAllByPage(pageable));
    }
}
