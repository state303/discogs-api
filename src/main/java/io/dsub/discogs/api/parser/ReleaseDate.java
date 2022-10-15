package io.dsub.discogs.api.parser;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReleaseDate {
    @Builder.Default
    private final int year = -1;
    @Builder.Default
    private final int month = -1;
    @Builder.Default
    private final int day = -1;
    @Builder.Default
    private final boolean validDay = false;
    @Builder.Default
    private final boolean validMonth = false;
    @Builder.Default
    private final boolean validYear = false;
    @Builder.Default
    private final String source = null;

    public boolean isValid() {
        return this.validYear && this.validMonth && this.validDay;
    }

    public ReleaseDate withDay(int day, boolean validDay) {
        return new ReleaseDate(year, month, day, validDay, validMonth, validYear, source);
    }

    public ReleaseDate withMonth(int month, boolean validMonth) {
        return new ReleaseDate(year, month, day, validDay, validMonth, validYear, source);
    }

    public ReleaseDate withYear(int year, boolean validYear) {
        return new ReleaseDate(year, month, day, validDay, validMonth, validYear, source);
    }

    public ReleaseDate withSource(String source) {
        return new ReleaseDate(year, month, day, validDay, validMonth, validYear, source);
    }

    public LocalDate toLocalDate() {
        int y = this.year < 0 ? 0 : this.year;
        int m = this.month < 1 ? 1 : this.month;
        int d = this.day < 1 ? 1 : this.day;
        return LocalDate.of(y, m, d);
    }
}
