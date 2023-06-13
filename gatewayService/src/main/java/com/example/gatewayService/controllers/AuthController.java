package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserLogin;
import com.example.gatewayService.security.JWTUtil;
import com.example.gatewayService.services.UserService;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.UserUtil.UserDTOUniqueValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserDTOUniqueValidator userDTOUniqueValidator;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          UserDTOUniqueValidator userDTOUniqueValidator,
                          JWTUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userDTOUniqueValidator = userDTOUniqueValidator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public String login(@RequestBody UserLogin userLogin) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
            return jwtUtil.generateToken(userLogin.getEmail());
        } catch (BadCredentialsException e) {
            throw new ErrorResponseException(new ErrorResponse());
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid UserLogin userLogin) {
        ErrorResponse errorResponse = new ErrorResponse();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userLogin.getEmail());
        userDTOUniqueValidator.validate(userDTO, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            userDTO.setPassword(userLogin.getPassword());
            userService.save(userDTO);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        throw new ErrorResponseException(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ErrorResponseException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
}
