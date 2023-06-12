package com.example.Frontend.service;

import com.example.Frontend.security.JWTUtil;
import com.example.Frontend.security.UserLoginDetails;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserLoginDetailsService userLoginDetailsService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserLoginDetailsService userLoginDetailsService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userLoginDetailsService = userLoginDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticateAndGetToken(String email, String password) throws InvalidCredentialsException {
        // Проверка правильности введенных учетных данных
        UserLoginDetails userLoginDetails = userLoginDetailsService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userLoginDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Создание и выдача JWT токена
        return jwtUtil.generateToken(userLoginDetails.getUsername());
    }
}


