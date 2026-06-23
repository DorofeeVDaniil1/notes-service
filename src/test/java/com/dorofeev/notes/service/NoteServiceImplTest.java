package com.dorofeev.notes.service;

import com.dorofeev.notes.dto.CreateNoteRequest;
import com.dorofeev.notes.dto.NoteResponse;
import com.dorofeev.notes.dto.UpdateNoteRequest;
import com.dorofeev.notes.entity.Note;
import com.dorofeev.notes.exception.NoteNotFoundException;
import com.dorofeev.notes.mapper.NoteMapper;
import com.dorofeev.notes.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    void createNote_shouldNormalizeTagsAndReturnResponse() {
        var request = new CreateNoteRequest("Title", "Content", Set.of(" Work ", "java", "JAVA"));
        var note = Note.builder()
                .title("Title")
                .content("Content")
                .tags(new LinkedHashSet<>(request.tags()))
                .build();
        var savedNote = note(1L, "Title", "Content", Set.of("work", "java"));
        var response = response(savedNote);

        when(noteMapper.toEntity(request)).thenReturn(note);
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);
        when(noteMapper.toResponse(savedNote)).thenReturn(response);

        var result = noteService.createNote(request);

        var noteCaptor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(noteCaptor.capture());

        assertThat(noteCaptor.getValue().getTags()).containsExactlyInAnyOrder("work", "java");
        assertThat(result).isEqualTo(response);
    }

    @Test
    void getNote_whenNoteDoesNotExist_shouldThrowException() {
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getNote(1L))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessage("Note not found with id: 1");

        verify(noteMapper, never()).toResponse(any(Note.class));
    }

    @Test
    void getNotes_withTag_shouldFilterByNormalizedTag() {
        var note = note(1L, "Title", "Content", Set.of("work"));
        var response = response(note);

        when(noteRepository.findByTag("work")).thenReturn(List.of(note));
        when(noteMapper.toResponse(note)).thenReturn(response);

        var result = noteService.getNotes(Optional.of(" Work "));

        assertThat(result).containsExactly(response);
        verify(noteRepository).findByTag("work");
        verify(noteRepository, never()).findAll();
    }

    @Test
    void updateNote_whenNoteExists_shouldUpdateAndReturnResponse() {
        var request = new UpdateNoteRequest("Updated", "New content", Set.of(" Spring ", "api"));
        var existingNote = note(1L, "Old", "Old content", Set.of("old"));
        var savedNote = note(1L, "Updated", "New content", Set.of("spring", "api"));
        var response = response(savedNote);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(savedNote);
        when(noteMapper.toResponse(savedNote)).thenReturn(response);

        var result = noteService.updateNote(1L, request);

        verify(noteMapper).updateEntity(request, existingNote);
        verify(noteRepository).save(existingNote);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void deleteNote_whenNoteDoesNotExist_shouldThrowException() {
        when(noteRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> noteService.deleteNote(1L))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessage("Note not found with id: 1");

        verify(noteRepository, never()).deleteById(1L);
    }

    private Note note(Long id, String title, String content, Set<String> tags) {
        return Note.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(new LinkedHashSet<>(tags))
                .createdAt(Instant.parse("2026-06-23T00:00:00Z"))
                .updatedAt(Instant.parse("2026-06-23T00:00:00Z"))
                .build();
    }

    private NoteResponse response(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getTags(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
