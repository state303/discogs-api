package io.dsub.discogs.api.style.service;

import io.dsub.discogs.api.style.command.StyleCommand;
import io.dsub.discogs.api.style.dto.StyleDTO;
import reactor.core.publisher.Mono;

public interface StyleService {
    Mono<StyleDTO> save(StyleCommand.Create command);

    Mono<Void> delete(StyleCommand.Delete command);
}
