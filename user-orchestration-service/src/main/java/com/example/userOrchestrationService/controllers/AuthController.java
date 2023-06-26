package com.example.userOrchestrationService.controllers;

import com.example.userOrchestrationService.dto.UserLogin;
import com.example.userOrchestrationService.security.JWTUtil;
import com.example.userOrchestrationService.services.UserService;
import com.example.userOrchestrationService.util.ErrorResponse;
import com.example.userOrchestrationService.util.ErrorResponseException;
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
@RequestMapping("/api/v1/auth")
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
        authenticateUser(userLogin.getEmail(), userLogin.getPassword());
        String email = userLogin.getEmail();
        String token = jwtUtil.generateToken(email);
        String role = getUserRole(email);
        return buildAuthResponse(token, role);
    }

    private void authenticateUser(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new ErrorResponseException(new ErrorResponse());
        }
    }

    private String getUserRole(String email) {
        return userService.findByEmail(email).getResponse().get(0).getUserRole().name();
    }

    private Map<String, String> buildAuthResponse(String token, String role) {
        Map<String, String> authMap = new HashMap<>();
        authMap.put("token", token);
        authMap.put("role", role);
        return authMap;
    }

}
