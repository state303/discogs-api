package io.dsub.discogs.api.label.dto;

import lombok.Builder;

@Builder
public record LabelDTO(long id, String contactInfo, String dataQuality, String name, String profile) {}
