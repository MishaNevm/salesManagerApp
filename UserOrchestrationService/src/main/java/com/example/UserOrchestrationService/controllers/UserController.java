package com.example.UserOrchestrationService.controllers;

import com.example.UserOrchestrationService.dto.UserDTO;
import com.example.UserOrchestrationService.dto.UserDTOResponse;
import com.example.UserOrchestrationService.services.UserService;
import com.example.UserOrchestrationService.util.ErrorResponse;
import com.example.UserOrchestrationService.util.ErrorResponseException;
import com.example.UserOrchestrationService.util.UserUtil.UserDTOUniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserDTOUniqueValidator userDTOUniqueValidator;

    @Autowired
    public UserController(UserService userService, UserDTOUniqueValidator userDTOUniqueValidator) {
        this.userService = userService;
        this.userDTOUniqueValidator = userDTOUniqueValidator;
    }

    @GetMapping()
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
        ErrorResponse errorResponse = new ErrorResponse();
        userDTOUniqueValidator.validate(user, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            userService.save(user);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        throw new ErrorResponseException(errorResponse);

    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody UserDTO user) {
        user.setId(id);
        ErrorResponse errorResponse = new ErrorResponse();
        userDTOUniqueValidator.validate(user, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            userService.update(user);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        throw new ErrorResponseException(errorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

