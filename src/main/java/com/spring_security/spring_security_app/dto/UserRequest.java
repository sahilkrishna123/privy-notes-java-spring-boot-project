package com.spring_security.spring_security_app.dto;

import com.spring_security.spring_security_app.user.Role;

public class UserRequest {
    private String username;
    private String password;
    private Role role;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
