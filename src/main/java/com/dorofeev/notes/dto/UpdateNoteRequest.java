package com.dorofeev.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateNoteRequest(
        @NotBlank(message = "Title must not be blank")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        @NotBlank(message = "Content must not be blank")
        @Size(max = 4000, message = "Content must be at most 4000 characters")
        String content,

        Set<@NotBlank(message = "Tag must not be blank") String> tags
) {
}
