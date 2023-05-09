package com.example.clientService.util.bankUtil;

public class BankNotSaveException extends RuntimeException{
    public BankNotSaveException(String message) {
        super(message);
    }
}
