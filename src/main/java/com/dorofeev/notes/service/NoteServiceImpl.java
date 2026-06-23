package com.dorofeev.notes.service;

import com.dorofeev.notes.dto.CreateNoteRequest;
import com.dorofeev.notes.dto.NoteResponse;
import com.dorofeev.notes.dto.UpdateNoteRequest;
import com.dorofeev.notes.entity.Note;
import com.dorofeev.notes.exception.NoteNotFoundException;
import com.dorofeev.notes.mapper.NoteMapper;
import com.dorofeev.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    @Transactional
    public NoteResponse createNote(CreateNoteRequest request) {
        var note = noteMapper.toEntity(request);
        note.setTags(normalizeTags(note.getTags()));

        var savedNote = noteRepository.save(note);
        log.info("Note created id={}", savedNote.getId());

        return noteMapper.toResponse(savedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse getNote(Long id) {
        return noteRepository.findById(id)
                .map(noteMapper::toResponse)
                .orElseThrow(() -> noteNotFound(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> getNotes(Optional<String> tag) {
        return tag.map(String::trim)
                .filter(value -> !value.isBlank())
                .map(this::normalizeTag)
                .map(noteRepository::findByTag)
                .orElseGet(noteRepository::findAll)
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long id, UpdateNoteRequest request) {
        var note = noteRepository.findById(id)
                .orElseThrow(() -> noteNotFound(id));

        noteMapper.updateEntity(request, note);
        note.setTags(normalizeTags(note.getTags()));

        var savedNote = noteRepository.save(note);
        log.info("Note updated id={}", savedNote.getId());

        return noteMapper.toResponse(savedNote);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw noteNotFound(id);
        }

        noteRepository.deleteById(id);
        log.info("Note deleted id={}", id);
    }

    private NoteNotFoundException noteNotFound(Long id) {
        log.warn("Note not found id={}", id);
        return new NoteNotFoundException(id);
    }

    private Set<String> normalizeTags(Set<String> tags) {
        if (tags == null) {
            return new LinkedHashSet<>();
        }

        return tags.stream()
                .map(this::normalizeTag)
                .filter(tag -> !tag.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String normalizeTag(String tag) {
        return Optional.ofNullable(tag)
                .map(String::trim)
                .map(String::toLowerCase)
                .orElse("");
    }
}
