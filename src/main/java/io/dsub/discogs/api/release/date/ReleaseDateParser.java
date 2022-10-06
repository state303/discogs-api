package io.dsub.discogs.api.release.date;

import io.dsub.discogs.api.release.model.Release;

public interface ReleaseDateParser {
    ReleaseDate parse(Release release);
}
