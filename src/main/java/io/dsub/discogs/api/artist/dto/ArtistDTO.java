package io.dsub.discogs.api.artist.dto;

import lombok.Builder;

@Builder
public record ArtistDTO (Integer id, String name, String realName, String profile, String dataQuality) {}
