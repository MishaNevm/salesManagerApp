package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserLogin;
import com.example.gatewayService.security.JWTUtil;
import com.example.gatewayService.services.UserService;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserLogin userLogin) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
            Map<String, String> authMap = new HashMap<>();
            authMap.put("token", jwtUtil.generateToken(userLogin.getEmail()));
            authMap.put("role", userService.findByEmail(userLogin.getEmail()).getResponse().get(0).getUserRole().name());
            return authMap;
        } catch (BadCredentialsException e) {
            throw new ErrorResponseException(new ErrorResponse());
        }
    }
}
