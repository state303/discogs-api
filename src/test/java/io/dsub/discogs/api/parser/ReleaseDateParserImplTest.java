package io.dsub.discogs.api.parser;

import io.dsub.discogs.api.test.ConcurrentTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReleaseDateParserImplTest extends ConcurrentTest {
    ReleaseDateParserImpl parser = new ReleaseDateParserImpl();

    @Test
    void parseHandlesValidVagueEntry() {
        var date = parser.parse("19xx301");
        assertFalse(date.isValid());
        assertFalse(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1900);
        assertThat(date.getMonth()).isEqualTo(3);
        assertThat(date.getDay()).isEqualTo(1);
    }

    @Test
    void parseHandlesInvalidVagueEntry() {
        var date = parser.parse("19314030");
        assertFalse(date.isValid());
        assertTrue(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1931);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(1931, 1, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-", " -", "- ", " ", "  ", "  _", " _", "_ ", ".", " .", ". ", ""})
    void parseHandlesKnownDelimiters(String delimiter) {
        String target = String.join(delimiter, "1992", "03", "01");
        var date = parser.parse(target);
        assertTrue(date.isValidYear());
        assertTrue(date.isValidMonth());
        assertTrue(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1992);
        assertThat(date.getMonth()).isEqualTo(3);
        assertThat(date.getDay()).isEqualTo(1);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(1992, 3, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "     ", "aaaa-dd-ww"})
    void parseHandlesEmptyBlankOrNonDigitOnlyString(String target) {
        var date = parser.parse(target);
        assertFalse(date.isValid());
        assertFalse(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(-1);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.getSource()).isIn(target, null);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(0,1,1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-", "  ", "_ ", "_" ,"", ".", " .", ".0", "0", "-0"})
    void parseHandlesYearMonthOnlyString(String delim) {
        String target = String.join(delim, "1991", "1");
        var date = parser.parse(target);
        assertFalse(date.isValid());
        assertTrue(date.isValidYear());
        assertTrue(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1991);
        assertThat(date.getMonth()).isEqualTo(1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.getSource()).isEqualTo(target);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(1991,1,1));
    }

    @Test
    void parseHandlesYearOnlyString() {
        var date = parser.parse("1991");
        assertFalse(date.isValid());
        assertTrue(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1991);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.getSource()).isEqualTo("1991");
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(1991,1,1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abc", "1", "3", "44", "12 "})
    void parseHandlesStringShorterThanFour(String target) {
        var date = parser.parse(target);
        assertFalse(date.isValid());
        assertFalse(date.isValidDay());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidYear());
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.getYear()).isEqualTo(-1);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getSource()).isEqualTo(target);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(0,1,1));
    }

    @Test
    void parseHandlesInvalidMonth() {
        var date = parser.parse("1922.x.3");
        assertFalse(date.isValid());
        assertTrue(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(1922);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.getSource()).isEqualTo("1922.x.3");
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(1922,1,1));
    }

    @Test
    void parseHandlesNull() {
        var date = parser.parse(null);
        assertFalse(date.isValid());
        assertFalse(date.isValidYear());
        assertFalse(date.isValidMonth());
        assertFalse(date.isValidDay());
        assertThat(date.getYear()).isEqualTo(-1);
        assertThat(date.getMonth()).isEqualTo(-1);
        assertThat(date.getDay()).isEqualTo(-1);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(0,1,1));
    }
}