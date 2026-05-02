package com.spring_security.spring_security_app.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteRepository repository;

    @GetMapping("/hello-notes")
    public String helloNotes(){
        return "Hello Notes";
    }
    @GetMapping()
    public List<Note> getNotes(){
        return repository.findAll();
    }

    @PostMapping("/create")
    public Note create(@RequestBody Note note){
        return repository.save(note);
    }
}
