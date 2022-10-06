package io.dsub.discogs.api.release.dto;

import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;

@With
@Builder
public record ReleaseDTO(
        Long id,
        Long masterId,
        String country,
        String dataQuality,
        String notes,
        String status,
        String title,
        String releaseDateReported,
        LocalDateTime releaseDate,
        Boolean isMaster,
        Boolean hasValidDay,
        Boolean hasValidMonth,
        Boolean hasValidYear) {
}
