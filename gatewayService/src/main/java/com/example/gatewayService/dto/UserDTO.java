package com.example.gatewayService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

//    @NotEmpty(message = "Дата рождения должна быть не пустой")
    @Past(message = "Дата рождения должна быть в прошлом")
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
