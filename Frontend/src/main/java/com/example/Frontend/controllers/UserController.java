package com.example.Frontend.controllers;


import com.example.Frontend.dto.UserDTO;
import com.example.Frontend.dto.UserDTOResponse;
import com.example.Frontend.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String GET_ALL_USERS = "http://localhost:8484/users";
    private final String GET_USER_BY_EMAIL = "http://localhost:8484/users?email=%s";
    private final String GET_USER_BY_ID = "http://localhost:8484/users/%d";

    private final String CREATE_USER = GET_ALL_USERS;
    private final String UPDATE_USER = GET_USER_BY_ID;
    private final String DELETE_USER = GET_USER_BY_ID;

    public UserController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping()
    public String getAllUsers(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email == null) {
            model.addAttribute("users", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_USERS, UserDTOResponse.class)).getResponse());
            return "user/getAllUsers";
        } else {
            model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(String.format(GET_USER_BY_EMAIL, email), UserDTO.class)));
            return "user/getUserById";
        }
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(String.format(GET_USER_BY_ID, id), UserDTO.class)));
        return "user/getUserById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("user") UserDTO userDTO) {
        return "user/createUser";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/createUser";
        }
        try {
            restTemplate.postForObject(CREATE_USER, userDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "user/createUser";
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) throws InterruptedException {
        model.addAttribute("user", restTemplate.getForObject(String.format( GET_USER_BY_ID, id), UserDTO.class));
        return "user/updateUser";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/updateUser";
        }
        try {
            restTemplate.patchForObject(String.format(UPDATE_USER, id), user, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "user/updateUser";
        }
        return "redirect:/users";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.delete (String.format(DELETE_USER, id));
        return "redirect:/users";
    }
}

