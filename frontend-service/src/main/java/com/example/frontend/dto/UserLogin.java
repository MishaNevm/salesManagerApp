package com.example.frontend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserLogin {
    @NotEmpty(message = "Почта должна быть не пустой")
    @Email(message = "Почта должна быть в формате текст@текст.com/ru")
    private String email;

    @NotEmpty(message = "Пароль не долен быть путым")
    @Pattern(regexp = "[a-z]*[A-Z][a-z]*[0-9]{3,}[a-z]*", message = "Пароль должен быть из латинских букв и содержать 1 заглавную букву и 3 цифры")
    private String password;

    private String userRole;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLogin() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
