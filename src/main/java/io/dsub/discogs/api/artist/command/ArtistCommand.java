package io.dsub.discogs.api.artist.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

public class ArtistCommand {
    @Getter
    @RequiredArgsConstructor
    public static class CreateArtistCommand {
        @NotNull
        private final Integer id;
        private final String name;
        private final String realName;
        private final String profile;
        private final String dataQuality;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UpdateArtistCommand {
        private final String name;
        private final String realName;
        private final String profile;
        private final String dataQuality;
    }
}
