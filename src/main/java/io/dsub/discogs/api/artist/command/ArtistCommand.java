package io.dsub.discogs.api.artist.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

public class ArtistCommand {
    @Getter
    @RequiredArgsConstructor
    public static class Create {
        @NotNull
        private final Integer id;
        @NotNull
        private final String name;
        private final String realName;
        private final String profile;
        private final String dataQuality;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Update {
        @NotNull
        private final String name;
        private final String realName;
        private final String profile;
        private final String dataQuality;
    }
}
