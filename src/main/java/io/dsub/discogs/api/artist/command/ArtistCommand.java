package io.dsub.discogs.api.artist.command;

import io.dsub.discogs.api.artist.model.Artist;
import lombok.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class ArtistCommand {
    @With
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
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

        public Mono<Artist> toEntity() {
            return Mono.just(Artist.builder()
                    .id(id)
                    .name(name)
                    .realName(realName)
                    .profile(profile)
                    .dataQuality(dataQuality)
                    .build());
        }
    }

    @With
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Update {
        @Size(max = 1000)
        private final String name;
        @Size(max = 2000)
        private final String realName;
        private final String profile;
        @Size(max = 255)
        private final String dataQuality;

        public Mono<Artist> toEntity(Long id) {
            return Mono.just(Artist.builder()
                    .id(id)
                    .name(name)
                    .realName(realName)
                    .profile(profile)
                    .dataQuality(dataQuality)
                    .build());
        }
    }
}
