package com.example.Frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final HttpServletRequest request;

    @Autowired
    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public RedirectView handleUnauthorizedException()  {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return new RedirectView("/auth/login");
    }
}

