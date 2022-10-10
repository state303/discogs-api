package io.dsub.discogs.api.release.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class ReleaseCommand {
    @Getter
    @AllArgsConstructor
    public static final class Create {
        @Min(0)
        @NotNull
        private final Long id;
        @Min(0)
        private final Long masterId;
        @Size(max = 255)
        private final String country;
        @Size(max = 255)
        private final String dataQuality;
        private final String notes;
        @Size(max = 255)
        private final String status;
        @Size(max = 10000)
        private final String title;
        @Size(max = 255)
        private final String releaseDate;
        private final Boolean isMaster;
    }

    @Getter
    @AllArgsConstructor
    public static final class Update {
        @Min(0)
        private final Long masterId;
        @Size(max = 255)
        private final String country;
        @Size(max = 255)
        private final String dataQuality;
        private final String notes;
        @Size(max = 255)
        private final String status;
        @Size(max = 10000)
        private final String title;
        @Size(max = 255)
        private final String releaseDate;
        private final Boolean isMaster;
    }

    @Getter
    @AllArgsConstructor
    public static final class DeleteByID {
        @Min(0)
        @NotNull
        private final Long id;
    }
}
