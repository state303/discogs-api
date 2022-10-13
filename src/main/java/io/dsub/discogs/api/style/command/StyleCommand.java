package io.dsub.discogs.api.style.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class StyleCommand {

    @Getter
    @RequiredArgsConstructor
    public static class Create extends StyleCommand {
        @NotNull
        @NotBlank
        @Size(max = 255)
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Delete extends StyleCommand {
        @NotNull
        @NotBlank
        @Size(max = 255)
        private final String name;
    }
}
