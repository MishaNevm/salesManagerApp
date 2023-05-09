package com.example.bankService.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {
    private String message;
    private long timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static String convertErrorsToMessage (BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError error : errorList) {
            errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
        }
        return errorMessage.toString();
    }
}
