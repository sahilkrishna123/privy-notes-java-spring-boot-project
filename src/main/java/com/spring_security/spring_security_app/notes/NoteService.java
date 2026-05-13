package com.spring_security.spring_security_app.notes;

import com.spring_security.spring_security_app.user.User;
import com.spring_security.spring_security_app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.spring_security.spring_security_app.notes.dto.NoteRequest;
import com.spring_security.spring_security_app.notes.dto.NoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Note> getNotesForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return noteRepository.findByUserId(user.getId());
    }
    // ── Helpers ──────────────────────────────────────────────

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private User resolveUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    private Note resolveNote(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Note not found: " + id));
    }

    private NoteResponse toResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getUser().getUsername()
        );
    }

    // ── Operations ───────────────────────────────────────────

    // Create — user resolved from JWT, never from request body
    public NoteResponse create(NoteRequest request) {
        User user = resolveUser(getCurrentUsername());

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUser(user);  // ← set from JWT, not client

        return toResponse(noteRepository.save(note));
    }

    // Get my notes
    public List<NoteResponse> getMyNotes() {
        User user = resolveUser(getCurrentUsername());
        return noteRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get all (admin)
    public List<NoteResponse> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get notes by userId (admin)
    public List<NoteResponse> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get one — owner or admin only
    public NoteResponse getOne(Long id) {
        Note note = resolveNote(id);

        if (!isAdmin() && !note.getUser().getUsername().equals(getCurrentUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return toResponse(note);
    }

    // Update — owner or admin only
    public NoteResponse update(Long id, NoteRequest request) {
        Note note = resolveNote(id);

        if (!isAdmin() && !note.getUser().getUsername().equals(getCurrentUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        return toResponse(noteRepository.save(note));
    }

    // Delete — owner or admin only
    public void delete(Long id) {
        Note note = resolveNote(id);

        if (!isAdmin() && !note.getUser().getUsername().equals(getCurrentUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        noteRepository.delete(note);
    }
}
