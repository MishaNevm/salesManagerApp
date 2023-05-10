package com.example.gatewayService.util;

public enum MethodsCodes {

    GET_ALL_USERS(0),
    GET_USER_BY_ID(1),
    CREATE_USER(2),
    UPDATE_USER(3),
    DELETE_USER(4);

    private final int code;

    public int getCode() {
        return code;
    }

    MethodsCodes(int code) {
        this.code = code;
    }


//    authenticateUser
//            authorizeUser
//    createOrder
//            updateOrder
//    deleteOrder
//            getOrderById
//    getOrdersByUser
//            getProductById
//    updateProductQuantity
//            getInventory
//    reserveProduct
//            createClient
//    updateClient
//            getClientById
//    getOrdersByClient
    }
