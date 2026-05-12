package com.spring_security.spring_security_app.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteRepository repository;

    // Create Note
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Note> create(@RequestBody Note note){
        return ResponseEntity.status(201).body(repository.save(note));
    }
    // Get all
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")  // Only admins see all notes
    public List<Note> getAll(){
        return repository.findAll();
    }
    // Get One
    @GetMapping("/{id}")
    public Note getOne(@PathVariable Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
    }
    // Update Note
    @PatchMapping("/{id}")
    public Note updateOne(@PathVariable Long id, @RequestBody Note note){
        Note existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        existing.setTitle(note.getTitle());
        existing.setContent(note.getContent());
        return repository.save(existing);
    }

}
