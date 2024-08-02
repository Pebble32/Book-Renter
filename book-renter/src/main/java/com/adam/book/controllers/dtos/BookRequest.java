package com.adam.book.controllers.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
        Long id,
        @NotNull(message = "100")
        @NotBlank(message = "100")
        @NotEmpty(message = "100")
        String title,
        @NotNull(message = "101")
        @NotBlank(message = "101")
        @NotEmpty(message = "101")
        String authorName,
        @NotNull(message = "102")
        @NotBlank(message = "102")
        @NotEmpty(message = "102")
        String isbn,
        String synopsis,
        boolean shareable
) {
}
