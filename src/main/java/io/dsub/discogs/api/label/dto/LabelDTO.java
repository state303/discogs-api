package io.dsub.discogs.api.label.dto;

import lombok.*;

@With
@Data
@Builder
public final class LabelDTO {
    private final long id;
    private final String contactInfo;
    private final String dataQuality;
    private final String name;
    private final String profile;
}