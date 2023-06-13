package com.example.Frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

//    private final HttpServletRequest request;
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public GlobalExceptionHandler(HttpServletRequest request, RestTemplate restTemplate) {
//        this.request = request;
//        this.restTemplate = restTemplate;
//    }
//
//    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
//    public void handleUnauthorizedException(HttpServletResponse response, HttpClientErrorException.Unauthorized exception) throws ServletException {
//        if (Objects.equals(exception.getMessage(), "JWT token expired")) {
//            request.logout();
//        }
//    }
}

