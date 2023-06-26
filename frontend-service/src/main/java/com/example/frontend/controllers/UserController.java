package com.example.frontend.controllers;


import com.example.frontend.dto.UserDTO;
import com.example.frontend.dto.UserDTOResponse;
import com.example.frontend.util.CurrentUser;
import com.example.frontend.util.Urls;
import com.example.frontend.util.UserRoles;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/users")
public class UserController {
    private final RestTemplate restTemplate;
    private final CurrentUser currentUser;

    public UserController(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String getAllUsers(@RequestParam(value = "email", required = false) String email, Model model) {
        model.addAttribute("role", currentUser.getRole());
        if (email == null) {
            UserDTOResponse response = restTemplate.getForObject(Urls.GET_ALL_USERS.getUrl(), UserDTOResponse.class);
            model.addAttribute("users", response != null ? response.getResponse() : Collections.emptyList());
            return "user/getAllUsers";
        } else {
            UserDTO userDTO = restTemplate.getForObject(String.format(Urls.GET_USER_BY_EMAIL.getUrl(), email), UserDTO.class);
            model.addAttribute("user", userDTO);
            return "user/getUserById";
        }
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("role", currentUser.getRole());
        UserDTO userDTO = restTemplate.getForObject(String.format(Urls.GET_USER_BY_ID.getUrl(), id), UserDTO.class);
        model.addAttribute("user", userDTO);
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
    public String create(Model model ,@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult) {
        model.addAttribute("roles", UserRoles.values());
        if (bindingResult.hasErrors()) {
            return "user/createUser";
        }
        try {
            restTemplate.postForObject(Urls.CREATE_USER.getUrl(), userDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            GlobalExceptionHandler.handleBadRequestException(e, bindingResult);
            return "user/createUser";
        }

        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) {
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
            GlobalExceptionHandler.handleBadRequestException(e, bindingResult);
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

