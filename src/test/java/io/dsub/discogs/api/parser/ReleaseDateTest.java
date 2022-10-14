package io.dsub.discogs.api.parser;

import io.dsub.discogs.api.test.ConcurrentTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseDateTest extends ConcurrentTest {
    @Test
    void toLocalDateMustReturnNonNegativeValue() {
        var date = ReleaseDate.builder().year(-1).month(-1).day(-1).build().toLocalDate();
        assertThat(date.getYear()).isEqualTo(0);
        assertThat(date.getMonthValue()).isEqualTo(1);
        assertThat(date.getDayOfMonth()).isEqualTo(1);
    }

    @Test
    void toLocalDateMustAssignValidValues() {
        var date = ReleaseDate.builder()
                .year(1991)
                .month(3)
                .day(12)
                .validYear(true)
                .validMonth(true)
                .validDay(true)
                .build().toLocalDate();
        assertThat(date.getYear()).isEqualTo(1991);
        assertThat(date.getMonthValue()).isEqualTo(3);
        assertThat(date.getDayOfMonth()).isEqualTo(12);
    }
}