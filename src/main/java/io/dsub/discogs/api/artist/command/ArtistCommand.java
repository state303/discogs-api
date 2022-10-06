package io.dsub.discogs.api.artist.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class ArtistCommand {
    @Getter
    @RequiredArgsConstructor
    public static class Create extends ArtistCommand {
        @Min(0)
        @NotNull
        private final Long id;
        @Size(max = 1000)
        private final String name;
        @Size(max = 2000)
        private final String realName;
        private final String profile;
        @Size(max = 255)
        private final String dataQuality;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Update extends ArtistCommand {
        @Size(max = 1000)
        private final String name;
        @Size(max = 2000)
        private final String realName;
        private final String profile;
        @Size(max = 255)
        private final String dataQuality;
    }
}
