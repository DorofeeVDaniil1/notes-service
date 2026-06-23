package com.dorofeev.notes.service;

import com.dorofeev.notes.dto.CreateNoteRequest;
import com.dorofeev.notes.dto.NoteResponse;
import com.dorofeev.notes.dto.UpdateNoteRequest;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    NoteResponse createNote(CreateNoteRequest request);

    NoteResponse getNote(Long id);

    List<NoteResponse> getNotes(Optional<String> tag);

    NoteResponse updateNote(Long id, UpdateNoteRequest request);

    void deleteNote(Long id);
}
