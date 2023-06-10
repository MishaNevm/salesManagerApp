package com.example.clientService.util;

import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorResponse {
   List<ErrorMessage> fieldErrorList;

    public List<ErrorMessage> getFieldErrorList() {
        return fieldErrorList;
    }

    public void setFieldErrorList(List<ErrorMessage> fieldErrorList) {
        this.fieldErrorList = fieldErrorList;
    }
}
