package com.example.inventoryService.util;

public class ProductNotAddException extends RuntimeException{
    public ProductNotAddException (String message) {
        super(message);
    }
}
