package com.dorofeev.notes.repository;

import com.dorofeev.notes.entity.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepository {

    private final SpringDataNoteRepository springDataNoteRepository;

    @Override
    public Note save(Note note) {
        return springDataNoteRepository.save(note);
    }

    @Override
    public Optional<Note> findById(Long id) {
        return springDataNoteRepository.findById(id);
    }

    @Override
    public List<Note> findAll() {
        return springDataNoteRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Note> findByTag(String tag) {
        return springDataNoteRepository.findByTag(tag);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataNoteRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        springDataNoteRepository.deleteById(id);
    }
}
