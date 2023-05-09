package com.example.clientService.util;

public class ClientNotSaveException extends RuntimeException{
    public ClientNotSaveException(String message) {
        super(message);
    }
}
