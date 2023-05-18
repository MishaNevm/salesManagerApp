package com.example.userService.controllers;

import com.example.userService.dto.UserDTO;
import com.example.userService.dto.UserDTOResponse;
import com.example.userService.kafka.Producer;
import com.example.userService.services.UserService;
import com.example.userService.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

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
        producer.sendMessage(MethodsCodes.GET_ALL_USERS.getCode(), userService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessage(MethodsCodes.GET_USER_BY_ID.getCode(), userService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<HttpStatus> findByEmail(@RequestParam("email") String email) {
        producer.sendMessage(MethodsCodes.GET_USER_BY_EMAIL.getCode(), userService.findByEmail(email));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO user) {
        userService.save(modelMapper.convertUserDTOToUser(user));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO user) {
        userService.findById(user.getId());
//        userDTOUniqueValidator.validate(user, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new UserNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        userService.update(modelMapper.convertUserDTOToUser(user));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.findById(id);
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(UserNotSaveException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Данный пользователь не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponse("Дата рожения должна быть в формате гггг-мм-дд")
                , HttpStatus.BAD_REQUEST);
    }
}
