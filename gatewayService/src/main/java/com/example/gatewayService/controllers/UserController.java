package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.service.UserStorageService;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserStorageService userStorageService;
    private final Producer producer;
    private UserDTO userDTO;

    public UserController(UserStorageService userStorageService, Producer producer) {
        this.userStorageService = userStorageService;
        this.producer = producer;
    }

    @GetMapping()
    public ResponseEntity<UserDTOResponse> getAllUsers() {
        producer.sendRequestToUserService(MethodsCodes.GET_ALL_USERS);
        userStorageService.waitForFlagChange();
        return ResponseEntity.ok(userStorageService.getUserDTOResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") int id) {
        userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_ID, userDTO);
        userStorageService.waitForFlagChange();
        return ResponseEntity.ok(userStorageService.getUserDTOResponse().getResponse().get(0));
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam(value = "email", required = false) String email) {
        userDTO = new UserDTO();
        userDTO.setEmail(email);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_EMAIL, userDTO);
        userStorageService.waitForFlagChange();
        return ResponseEntity.ok(userStorageService.getUserDTOResponse().getResponse().get(0));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO user, BindingResult bindingResult) {
        producer.sendRequestToUserService(MethodsCodes.CREATE_USER, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody @Valid UserDTO user, BindingResult bindingResult) {
        user.setId(id);
        producer.sendRequestToUserService(MethodsCodes.UPDATE_USER, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.DELETE_USER, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

