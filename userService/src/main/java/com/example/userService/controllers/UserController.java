package com.example.userService.controllers;

import com.example.userService.dto.UserDTO;
import com.example.userService.services.UserService;
import com.example.userService.util.ErrorResponse;
import com.example.userService.util.ModelMapperUtil;
import com.example.userService.util.UserDTOUniqueValidator;
import com.example.userService.util.UserNotFoundException;
import com.example.userService.util.UserNotSaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ModelMapperUtil modelMapper;
    private final UserService userService;
    private final UserDTOUniqueValidator userDTOUniqueValidator;

    @Autowired
    public UserController(ModelMapperUtil modelMapper, UserService userService, UserDTOUniqueValidator userDTOUniqueValidator) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userDTOUniqueValidator = userDTOUniqueValidator;
    }

    @GetMapping()
    public List<UserDTO> findAll() {
        return userService.findAll().stream().map(modelMapper::convertUserToUserDTO).toList();
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") int id) {
        return modelMapper.convertUserToUserDTO(userService.findById(id));
    }

    @GetMapping("/findByEmail")
    public UserDTO findByEmail (@RequestParam("email") String email) {
        if (email.isEmpty()) throw new UserNotFoundException();
        return modelMapper.convertUserToUserDTO(userService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO user, BindingResult bindingResult) {
        userDTOUniqueValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        userService.save(modelMapper.convertUserDTOToUser(user));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody @Valid UserDTO user, BindingResult bindingResult) {
        userService.findById(id);
        userDTOUniqueValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new UserNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
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
    public ResponseEntity<ErrorResponse> exceptionHandler (HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponse("Дата рожения должна быть в формате гггг-мм-дд")
                , HttpStatus.BAD_REQUEST);
    }

}
