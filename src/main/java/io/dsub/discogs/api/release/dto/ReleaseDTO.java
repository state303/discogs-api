package io.dsub.discogs.api.release.dto;

import lombok.*;

import java.time.LocalDate;

@With
@Data
@Builder
public final class ReleaseDTO {
    private final Long id;
    private final Long masterId;
    private final String country;
    private final String dataQuality;
    private final String notes;
    private final String status;
    private final String title;
    private final String listedReleaseDate;
    private final LocalDate releaseDate;
    private final Boolean isMaster;
    private final Boolean hasValidDay;
    private final Boolean hasValidMonth;
    private final Boolean hasValidYear;
}
