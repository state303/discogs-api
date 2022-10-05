package io.dsub.discogs.api.style.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class StyleCommand {

    private StyleCommand(){}

    public abstract String getName();

    @Getter
    @RequiredArgsConstructor
    public static class Create {
        @NotNull
        @Size(min = 1, max = 255)
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Delete {
        @NotNull
        @Size(min = 1, max = 255)
        private final String name;
    }
}
