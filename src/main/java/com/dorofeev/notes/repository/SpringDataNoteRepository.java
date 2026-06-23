package com.dorofeev.notes.repository;

import com.dorofeev.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface SpringDataNoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT DISTINCT n FROM Note n JOIN n.tags t WHERE t = :tag ORDER BY n.createdAt DESC")
    List<Note> findByTag(@Param("tag") String tag);

    List<Note> findAllByOrderByCreatedAtDesc();
}
