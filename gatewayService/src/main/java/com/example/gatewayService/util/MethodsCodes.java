package com.example.gatewayService.util;

public enum MethodsCodes {

    GET_ALL_USERS(0),
    GET_USER_BY_ID(1),
    GET_USER_BY_EMAIL(2),
    CREATE_USER(3),
    UPDATE_USER(4),
    DELETE_USER(5);

    private final Integer code;

    public Integer getCode() {
        return code;
    }

    MethodsCodes(Integer code) {
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
