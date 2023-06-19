package com.example.Frontend.dto;


import com.example.Frontend.util.UserRoles;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

public class UserDTO {

    int id;

    @NotEmpty(message = "Имя должно быть не пустым")
    @Pattern(regexp = "[А-Я][а-я]{1,29}", message = "Имя должно начинаться с большой буквы и быть от 2 до 30 букв")
    private String name;

    @NotEmpty(message = "Фамилия должна быть не пустой")
    @Pattern(regexp = "[А-Я][а-я]{1,29}", message = "Фамилия должна начинаться с большой буквы и быть от 2 до 30 букв")
    private String surname;

    @NotEmpty(message = "Отчество должно быть не пустым")
    @Pattern(regexp = "[А-Я][а-я]{1,29}", message = "Отчество должно начинаться с большой буквы и быть от 2 до 30 букв")
    private String patronymic;

    @NotEmpty(message = "Почта должна быть не пустой")
    @Email(message = "Почта должна быть в формате текст@текст.com/ru")
    private String email;

    @NotEmpty(message = "Пароль не долен быть путым")
    @Pattern(regexp = "[a-z]*[A-Z][a-z]*[0-9]{3,}[a-z]*", message = "Пароль должен быть из латинских букв и содержать 1 заглавную букву и 3 цифры")
    private String password;

    private UserRoles userRole;

    private Date createdAt;

    @Past(message = "Дата рождения должна быть в прошлом")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
