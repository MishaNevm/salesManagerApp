package com.example.userOrchestrationService.controllers;

import com.example.userOrchestrationService.dto.UserDTO;
import com.example.userOrchestrationService.dto.UserDTOResponse;
import com.example.userOrchestrationService.services.UserService;
import com.example.userOrchestrationService.util.UserUtil.UserDTOUniqueValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserDTOUniqueValidator userDTOUniqueValidator;

    public UserController(UserService userService, UserDTOUniqueValidator userDTOUniqueValidator) {
        this.userService = userService;
        this.userDTOUniqueValidator = userDTOUniqueValidator;
    }

    @GetMapping
    public UserDTOResponse getAllUsers(@RequestParam(value = "email", required = false) String email) {
        if (email == null) {
            return userService.findAll();
        } else {
            return userService.findByEmail(email);
        }
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody UserDTO user) {
        userDTOUniqueValidator.validate(user);
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody UserDTO user) {
        user.setId(id);
        userDTOUniqueValidator.validate(user);
        userService.update(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

