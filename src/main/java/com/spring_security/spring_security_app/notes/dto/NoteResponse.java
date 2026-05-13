package com.spring_security.spring_security_app.notes.dto;

public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private String ownerUsername;   // helpful, but no sensitive user data

    public NoteResponse(Long id, String title, String content, String ownerUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ownerUsername = ownerUsername;
    }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getOwnerUsername() { return ownerUsername; }
}