package com.example.Frontend.controllers;


import com.example.Frontend.dto.UserDTO;
import com.example.Frontend.dto.UserDTOResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {
    private final RestTemplate restTemplate;
    private final String GET_ALL_USERS = "http://localhost:8484/users";
    private final String GET_USER_BY_EMAIL = "http://localhost:8484/users?email=";
    private final String GET_USER_BY_ID = "http://localhost:8484/users/";

    private final String CREATE_USER = GET_ALL_USERS;
    private final String UPDATE_USER = GET_USER_BY_ID;
    private final String DELETE_USER = GET_USER_BY_ID;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping()
    public String getAllUsers(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email == null) {
            model.addAttribute("users", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_USERS, UserDTOResponse.class)).getResponse());
            return "user/getAllUsers";
        } else {
            model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(GET_USER_BY_EMAIL + email, UserDTO.class)));
            return "user/getUserById";
        }
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", Objects.requireNonNull(restTemplate.getForObject(GET_USER_BY_ID + id, UserDTO.class)));
        return "user/getUserById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("user") UserDTO userDTO) {
        return "user/createUser";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "user/createUser";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(CREATE_USER, HttpMethod.POST, entity, HttpStatus.class);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) throws InterruptedException {
        model.addAttribute("user", restTemplate.getForObject(GET_USER_BY_ID + id, UserDTO.class));
        return "user/updateUser";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/updateUser";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(UPDATE_USER + id, HttpMethod.PATCH, entity, HttpStatus.class);

        return "redirect:/users";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.exchange(DELETE_USER + id, HttpMethod.DELETE, null, HttpStatus.class);
        return "redirect:/users";
    }
}

