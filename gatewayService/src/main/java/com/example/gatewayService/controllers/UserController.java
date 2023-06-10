package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Producer producer;

    private final Consumer consumer;

    public UserController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @GetMapping()
    public UserDTOResponse getAllUsers(@RequestParam(value = "email", required = false) String email) throws InterruptedException {
        if (email == null) {
            producer.sendRequestToUserService(MethodsCodes.GET_ALL_USERS, null);
            return (UserDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_USERS).take();
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_EMAIL, userDTO);
            return (UserDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_EMAIL).take();
        }
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") int id) throws InterruptedException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_ID, userDTO);
        return (UserDTO) consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_ID)
                .take().getResponse().get(0);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO user) {
        producer.sendRequestToUserService(MethodsCodes.CREATE_USER, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody @Valid UserDTO user, BindingResult bindingResult) {
        user.setId(id);
        producer.sendRequestToUserService(MethodsCodes.UPDATE_USER, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.DELETE_USER, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

