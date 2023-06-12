package com.example.userService.controllers;

import com.example.userService.dto.UserDTO;
import com.example.userService.kafka.Producer;
import com.example.userService.services.UserService;
import com.example.userService.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Producer producer;

    private final UserService userService;
    private final UserDTOUniqueValidator userDTOUniqueValidator;
    private final ModelMapperUtil modelMapper;

    @Autowired
    public UserController(Producer producer, UserService userService, UserDTOUniqueValidator userDTOUniqueValidator, ModelMapperUtil modelMapper) {
        this.producer = producer;
        this.userService = userService;
        this.userDTOUniqueValidator = userDTOUniqueValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<HttpStatus> findAll() {
        producer.sendMessage(MethodsCodes.GET_ALL_USERS, userService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessage(MethodsCodes.GET_USER_BY_ID, userService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<HttpStatus> findByEmail(@RequestParam("email") String email) {
        producer.sendMessage(MethodsCodes.GET_USER_BY_EMAIL, userService.findByEmail(email));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public void create(@RequestBody @Valid UserDTO user) {
        ErrorResponse errorResponse = new ErrorResponse();
        userDTOUniqueValidator.validate(user, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            userService.save(modelMapper.convertUserDTOToUser(user));
        }
        producer.sendMessage(MethodsCodes.CREATE_USER, errorResponse);
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody @Valid UserDTO user) {
        ErrorResponse errorResponse = new ErrorResponse();
        userDTOUniqueValidator.validate(user, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            userService.update(user);
        }
        producer.sendMessage(MethodsCodes.UPDATE_USER, errorResponse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        userService.delete(id);
    }
}
