package com.dorofeev.notes.repository;

import com.dorofeev.notes.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {

    Note save(Note note);

    Optional<Note> findById(Long id);

    List<Note> findAll();

    List<Note> findByTag(String tag);

    boolean existsById(Long id);

    void deleteById(Long id);
}
