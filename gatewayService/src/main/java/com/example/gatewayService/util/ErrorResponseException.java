package com.example.gatewayService.util;

public class ErrorResponseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ErrorResponseException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
