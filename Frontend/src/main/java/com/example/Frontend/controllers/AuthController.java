package com.example.Frontend.controllers;

import com.example.Frontend.dto.UserLogin;
import com.example.Frontend.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static String LOGIN = "http://localhost:8484/auth/login";
    private static String REGISTRATION = "http://localhost:8484/auth/registration";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("userLogin") UserLogin userLogin) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userLogin") @Valid UserLogin userLogin, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        try {
           restTemplate.postForEntity(REGISTRATION, userLogin, Void.class);
        } catch (HttpClientErrorException.BadRequest e) {
            ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
            if (!errorResponse.getErrors().isEmpty()) {
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
                return "auth/registration";
            }
        }
        return "redirect:/auth/login";
    }
}
