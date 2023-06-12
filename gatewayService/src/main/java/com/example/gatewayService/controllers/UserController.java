package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

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
            return (UserDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_EMAIL).poll(15, TimeUnit.SECONDS);
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
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO user) throws InterruptedException {
        producer.sendRequestToUserService(MethodsCodes.CREATE_USER, user);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.CREATE_USER).poll(15, TimeUnit.SECONDS);
        if (errorResponse != null && !errorResponse.getErrors().isEmpty()) {
            throw new ErrorResponseException(errorResponse);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserDTO user) throws InterruptedException {
        producer.sendRequestToUserService(MethodsCodes.UPDATE_USER, user);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.UPDATE_USER).poll(15, TimeUnit.SECONDS);
        if (errorResponse != null && !errorResponse.getErrors().isEmpty()) {
            throw new ErrorResponseException(errorResponse);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.DELETE_USER, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ErrorResponseException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
}

