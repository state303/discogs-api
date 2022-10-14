package io.dsub.discogs.api.parser;

public interface ReleaseDateParser {
    ReleaseDate parse(String candidate);
}
