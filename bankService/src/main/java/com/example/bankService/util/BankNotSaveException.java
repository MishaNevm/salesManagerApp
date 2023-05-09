package com.example.bankService.util;

public class BankNotSaveException extends RuntimeException{
    public BankNotSaveException(String message) {
        super(message);
    }
}
