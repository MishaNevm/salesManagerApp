package com.example.Frontend.util;

import java.util.List;

public class ValidationException extends Exception {
    private List<ValidationError> errors;
    public List<ValidationError> getErrors() {
        return errors;
    }
    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }
}
