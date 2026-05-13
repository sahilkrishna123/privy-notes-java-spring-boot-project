package com.spring_security.spring_security_app.user.dto;

import com.spring_security.spring_security_app.user.Role;

public class UserResponse {
    private String username;
    private Role role;

    public UserResponse(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public Role getRole() { return role; }
}
