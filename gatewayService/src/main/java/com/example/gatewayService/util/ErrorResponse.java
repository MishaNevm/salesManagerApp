package com.example.gatewayService.util;

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
