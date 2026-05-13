package com.spring_security.spring_security_app.notes;

import com.spring_security.spring_security_app.notes.dto.NoteRequest;
import com.spring_security.spring_security_app.notes.dto.NoteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@Tag(
        name = "Notes Management",
        description = "Create, read, update, and delete notes."
)
public class NoteController {

    @Autowired
    private NoteService noteService;

    // GET /notes — admin sees all
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NoteResponse>> getAll() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    // GET /notes/me — my notes from JWT
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<NoteResponse>> getMyNotes() {
        return ResponseEntity.ok(noteService.getMyNotes());
    }

    // GET /notes/{id} — owner or admin
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getOne(id));
    }

    // GET /notes/user/{userId} — admin only
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NoteResponse>> getNotesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(noteService.getNotesByUserId(userId));
    }

    // POST /notes — create, user from JWT
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(201).body(noteService.create(request));
    }

    // PATCH /notes/{id} — owner or admin
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.ok(noteService.update(id, request));
    }

    // DELETE /notes/{id} — owner or admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}