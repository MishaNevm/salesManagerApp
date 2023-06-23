package com.example.gatewayService.controllers;

import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeUnit;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private final Consumer consumer;

    @Autowired
    public GlobalExceptionHandler(Consumer consumer) {
        this.consumer = consumer;
    }


    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> handleErrorResponseException(ErrorResponseException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    public void checkErrorResponse(MethodsCodes methodsCodes) throws InterruptedException {
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(methodsCodes).poll(15, TimeUnit.SECONDS);
        if (errorResponse != null && errorResponse.getErrors() != null && !errorResponse.getErrors().isEmpty()) {
            throw new ErrorResponseException(errorResponse);
        }
    }
}
