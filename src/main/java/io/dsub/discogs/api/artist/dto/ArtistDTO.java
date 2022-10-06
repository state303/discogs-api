package io.dsub.discogs.api.artist.dto;

import lombok.Builder;

@Builder
public record ArtistDTO (Long id, String name, String realName, String profile, String dataQuality) {}
