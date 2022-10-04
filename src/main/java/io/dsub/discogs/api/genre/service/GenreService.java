package io.dsub.discogs.api.genre.service;

import io.dsub.discogs.api.genre.command.GenreCommand;
import io.dsub.discogs.api.genre.model.Genre;
import reactor.core.publisher.Mono;

public interface GenreService {
    Mono<Genre> save(GenreCommand.Create command);

    Mono<Void> delete(GenreCommand.Delete command);
}
