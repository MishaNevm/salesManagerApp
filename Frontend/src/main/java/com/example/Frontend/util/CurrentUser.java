package com.example.Frontend.util;

import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}