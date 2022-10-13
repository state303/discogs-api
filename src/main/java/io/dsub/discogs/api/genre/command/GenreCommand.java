package io.dsub.discogs.api.genre.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class GenreCommand {
    @Getter
    @RequiredArgsConstructor
    public static class Create extends GenreCommand {
        @NotNull
        @NotBlank
        @Size(max = 255)
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Delete extends GenreCommand {
        @NotNull
        @NotBlank
        @Size(max = 255)
        private final String name;
    }
}
