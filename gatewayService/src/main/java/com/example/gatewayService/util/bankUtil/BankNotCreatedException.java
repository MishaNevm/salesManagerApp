package com.example.gatewayService.util.bankUtil;

import com.example.gatewayService.util.ErrorResponse;

public class BankNotCreatedException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public BankNotCreatedException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
