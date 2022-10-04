package io.dsub.discogs.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
class StringUtilTest {
    @Test
    void encodeToUTF8EncodesNonUTF8CompliantString() {
        String stringToTest = "@#!~test  __ -_ ;;";

        String got = StringUtil.encodeToUTF8(stringToTest);
        String expected = URLEncoder.encode(stringToTest, StandardCharsets.UTF_8);

        assertEquals(expected, got);
        assertNotEquals(stringToTest, got);
    }

    @Test
    void encodeToUTF8HandlesNullString() {
        assertNull(StringUtil.encodeToUTF8(null));
    }

    @Test
    void encodeToUTF8HandlesEmptyString() {
        assertEquals("", StringUtil.encodeToUTF8(""));
    }
}
