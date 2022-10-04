package io.dsub.discogs.api.artist.controller;

import io.dsub.discogs.api.Constants;
import io.dsub.discogs.api.artist.dto.ArtistDTO;
import io.dsub.discogs.api.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static io.dsub.discogs.api.artist.command.ArtistCommand.Create;
import static io.dsub.discogs.api.artist.command.ArtistCommand.Update;

@Slf4j
@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;

    private final int defaultPageIndex = Constants.DEFAULT_PAGE_INDEX;
    private final int defaultPageSize = Constants.DEFAULT_PAGE_SIZE;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Mono<Page<ArtistDTO>>> getArtistsByPage(
            final @PageableDefault(page = defaultPageIndex, size = defaultPageSize) Pageable page) {
        return ResponseEntity.ok(artistService.getArtistsByPageAndSize(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<ArtistDTO>> getArtistById(final @PathVariable Integer id) {
        return ResponseEntity.ok(artistService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Mono<ArtistDTO>> createArtist(final @RequestBody Create command) {
        return ResponseEntity.ok(artistService.updateOrInsert(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<ArtistDTO>> updateArtist(final @PathVariable Integer id, final @RequestBody Update command) {
        return ResponseEntity.ok(artistService.update(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deleteArtistById(final @PathVariable Integer id) {
        return ResponseEntity.ok(artistService.delete(id));
    }
}
