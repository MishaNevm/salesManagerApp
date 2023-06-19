package com.example.Frontend.util;

import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private String email;
    private String role;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}