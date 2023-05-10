package com.example.inventoryService.util;

public class ProductNotSaveException extends RuntimeException{
    public ProductNotSaveException(String message) {
        super(message);
    }
}
