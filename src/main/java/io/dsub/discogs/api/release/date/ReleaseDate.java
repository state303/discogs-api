package io.dsub.discogs.api.release.date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;

@With
@Getter
@Builder
@AllArgsConstructor
public class ReleaseDate {
    private final String listedReleaseDate;
    private final LocalDateTime releaseDate;
    private final boolean isMonthValid;
    private final boolean isYearValid;
    private final boolean isDayValid;
}