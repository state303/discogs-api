package io.dsub.discogs.api.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class StringUtil {
    private StringUtil() {}
    public static String encodeToUTF8(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
