package com.example.Frontend.controllers;

import com.example.Frontend.dto.UserLogin;
import com.example.Frontend.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static String LOGIN = "http://localhost:8484/auth/login";
    private static String REGISTRATION = "http://localhost:8484/auth/registration";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(RestTemplate restTemplate, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("userLogin") UserLogin userLogin) {
        return "auth/login";
    }

    @PostMapping()
    public String login(@ModelAttribute("userLogin") UserLogin userLogin,
                        BindingResult bindingResult,
                        HttpServletResponse response) {

        // Валидация полей формы
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        userLogin.setPassword(passwordEncoder.encode(userLogin.getPassword()));
        // Аутентификация пользователя
        String token;
        try {
            token = restTemplate.postForObject(LOGIN, userLogin, String.class);
        } catch (Exception e) {
            System.out.println(e.getClass());
            bindingResult.rejectValue("login", "0", "Неверный логин или пароль");
            return "auth/login";
        }
        addTokenToCookie(token, response);
        return "redirect:/users";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("userLogin") UserLogin userLogin) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userLogin") UserLogin userLogin, BindingResult bindingResult, HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        System.out.println(userLogin.getPassword());
        userLogin.setPassword(passwordEncoder.encode(userLogin.getPassword()));
        String token;
        try {
            token = restTemplate.postForObject(REGISTRATION, userLogin, String.class);
        } catch (HttpClientErrorException.BadRequest e) {
            ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
            errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            return "auth/registration";
        }
        // Создание куки с токеном
        addTokenToCookie(token, response);
        return "redirect:/users";
    }

    private void addTokenToCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(2 * 60 * 60); // Время жизни куки - 2 часа
        cookie.setHttpOnly(true); // Запрет доступа через JavaScript
        cookie.setPath("/"); // Ограничение доступа по пути
        response.addCookie(cookie);
        response.addCookie(cookie);
    }
}
