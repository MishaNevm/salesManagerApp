package com.example.clientService.util;

public class ValidationError {
    private final String field;
    private final String code;
    private final String message;

    public ValidationError(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
