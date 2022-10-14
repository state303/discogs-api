package io.dsub.discogs.api.genre.dto;

import lombok.*;

@With
@Data
@Builder
public final class GenreDTO {
    private final String name;
}
