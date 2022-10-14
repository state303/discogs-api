package io.dsub.discogs.api.label.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class LabelCommand {
    @Getter
    @AllArgsConstructor
    public static class Create {
        @Min(0)
        @NotNull
        private final Long id;
        @Size(max = 255)
        private final String name;
        private final String profile;
        @Size(max = 255)
        private final String dataQuality;
        private final String contactInfo;
        @Min(0)
        private final Long parentLabelId;
    }

    @Getter
    @AllArgsConstructor
    public static class Update {
        @Size(max = 255)
        private final String name;
        private final String profile;
        @Size(max = 255)
        private final String dataQuality;
        private final String contactInfo;
        @Min(0)
        private final Long parentLabelId;
    }
}
