package com.example.Frontend.controllers;


import com.example.Frontend.dto.UserDTO;
import com.example.Frontend.dto.UserDTOResponse;
import com.example.Frontend.util.CurrentUser;
import com.example.Frontend.util.ErrorResponse;
import com.example.Frontend.util.Urls;
import com.example.Frontend.util.UserRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CurrentUser currentUser;


    public UserController(RestTemplate restTemplate, ObjectMapper objectMapper, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currentUser = currentUser;
    }

    @GetMapping()
    public String getAllUsers(@RequestParam(value = "email", required = false) String email, Model model) {
        model.addAttribute("role", currentUser.getRole());
        if (email == null) {
            model.addAttribute("users", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_USERS.getUrl(), UserDTOResponse.class)).getResponse());
            return "user/getAllUsers";
        } else {
            model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(String.format(Urls.GET_USER_BY_EMAIL.getUrl(), email), UserDTO.class)));
            return "user/getUserById";
        }
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("role", currentUser.getRole());
        model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(String.format(Urls.GET_USER_BY_ID.getUrl(), id), UserDTO.class)));
        return "user/getUserById";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", UserRoles.values());
        return "user/createUser";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public String create(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/createUser";
        }
        try {
            restTemplate.postForObject(Urls.CREATE_USER.getUrl(), userDTO, HttpStatus.class);
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) throws InterruptedException {
        model.addAttribute("user", restTemplate.getForObject(String.format(Urls.GET_USER_BY_ID.getUrl(), id), UserDTO.class));
        model.addAttribute("roles", UserRoles.values());
        return "user/updateUser";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/updateUser";
        }
        try {
            restTemplate.patchForObject(String.format(Urls.UPDATE_USER.getUrl(), id), user, HttpStatus.class);
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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.delete(String.format(Urls.DELETE_USER.getUrl(), id));
        return "redirect:/users";
    }
}

