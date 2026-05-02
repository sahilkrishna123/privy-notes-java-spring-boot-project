package com.spring_security.spring_security_app.notes;

import com.spring_security.spring_security_app.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="notes")
public class Note {
    public Note() {
    }

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
