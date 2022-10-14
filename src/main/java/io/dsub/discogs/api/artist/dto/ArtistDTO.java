package io.dsub.discogs.api.artist.dto;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@With
@Data
@Builder
public final class ArtistDTO {
    private final Long id;
    private final String name;
    private final String realName;
    private final String profile;
    private final String dataQuality;
}