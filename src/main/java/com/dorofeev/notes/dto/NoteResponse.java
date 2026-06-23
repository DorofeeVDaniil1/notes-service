package com.dorofeev.notes.dto;

import java.time.Instant;
import java.util.Set;

public record NoteResponse(
        Long id,
        String title,
        String content,
        Set<String> tags,
        Instant createdAt,
        Instant updatedAt
) {
}
