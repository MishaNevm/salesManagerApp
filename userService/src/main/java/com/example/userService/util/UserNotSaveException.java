package com.example.userService.util;

public class UserNotSaveException extends RuntimeException{
    public UserNotSaveException(String message) {
        super(message);
    }
}
