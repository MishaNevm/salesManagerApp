package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.services.UserStorageService;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserStorageService userStorageService;
    private final Producer producer;

    public UserController(UserStorageService userStorageService, Producer producer) {
        this.userStorageService = userStorageService;
        this.producer = producer;
    }

    @GetMapping
    public UserDTOResponse getAllUsers() {
        producer.sendORequestToUserService(MethodsCodes.GET_ALL_USERS);
        return userStorageService.getUserDTO();
    }
}

