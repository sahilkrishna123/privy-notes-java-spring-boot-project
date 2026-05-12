package com.spring_security.spring_security_app.notes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // It has all built-in methods
}
