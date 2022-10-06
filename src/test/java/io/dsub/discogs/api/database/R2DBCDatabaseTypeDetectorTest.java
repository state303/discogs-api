package io.dsub.discogs.api.database;

import io.dsub.discogs.api.exception.InvalidR2DBCConnectionUrlException;
import io.dsub.discogs.api.exception.UnsupportedDatabaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class R2DBCDatabaseTypeDetectorTest {

    R2DBCDatabaseTypeDetector detector = new R2DBCDatabaseTypeDetector();

    @ParameterizedTest
    @ValueSource(strings = {"test", "", "r2dbc:postgres:", "r2dbc:postgresql", " "})
    void findTypeFromThrowsInvalidR2DBCConnectionUrlException(String urlParam) {
        Throwable t = catchThrowable(() -> detector.findTypeFrom(urlParam));
        assertThat(t).isInstanceOf(InvalidR2DBCConnectionUrlException.class);
        if (!urlParam.isBlank()) {
            assertThat(t.getMessage()).doesNotContain(urlParam);
        }
    }

    @Test
    void findTypeFromThrowsInvalidR2DBCConnectionUrlExceptionWhenNull() {
        Throwable t = catchThrowable(() -> detector.findTypeFrom(null));
        assertThat(t).isInstanceOf(InvalidR2DBCConnectionUrlException.class);
        assertThat(t.getMessage()).doesNotContain("null");
    }

    @Test
    void findTypeFromThrowsUnsupportedDatabaseException() {
        Throwable t = catchThrowable(() -> detector.findTypeFrom("r2dbc:postgres://hello-world"));
        assertThat(t).isInstanceOf(UnsupportedDatabaseException.class);
        assertThat(t.getMessage()).contains("database POSTGRES is not supported");
    }

    @Test
    void findTypeFrom() throws UnsupportedDatabaseException, InvalidR2DBCConnectionUrlException {
        DatabaseType type = detector.findTypeFrom("r2dbc:postgresql://");
        assertThat(type).isEqualTo(DatabaseType.POSTGRESQL);
    }
}
