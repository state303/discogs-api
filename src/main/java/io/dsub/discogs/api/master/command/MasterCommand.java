package io.dsub.discogs.api.master.command;

import io.dsub.discogs.api.master.model.Master;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MasterCommand {
    @Getter
    @AllArgsConstructor
    public static final class Create {
        @NotNull
        @Min(0)
        private final Long id;
        @Size(max = 255)
        private final String dataQuality;
        @Size(max = 2000)
        private final String title;
        @Min(0)
        @NotNull
        private final Short year;
    }

    @Getter
    @AllArgsConstructor
    public static final class Update {
        @Size(max = 255)
        private final String dataQuality;
        @Size(max = 2000)
        private final String title;
        @Min(0)
        @NotNull
        private final Short year;
    }
}
