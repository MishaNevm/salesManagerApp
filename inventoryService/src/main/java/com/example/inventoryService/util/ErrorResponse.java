package com.example.inventoryService.util;

import java.util.List;

public class ErrorResponse {
   List<ValidationError> errors;

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }
}
