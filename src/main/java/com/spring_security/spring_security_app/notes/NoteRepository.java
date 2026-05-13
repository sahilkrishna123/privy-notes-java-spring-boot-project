package com.spring_security.spring_security_app.notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // It has all built-in methods
    List<Note> findByUserId(Long userId);
}
