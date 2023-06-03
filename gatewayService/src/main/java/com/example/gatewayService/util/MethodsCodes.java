package com.example.gatewayService.util;

import java.util.Arrays;

public enum MethodsCodes {

    GET_ALL_USERS(0, true),
    GET_USER_BY_ID(1, true),
    GET_USER_BY_EMAIL(2, true),
    CREATE_USER(3, false),
    UPDATE_USER(4, false),
    DELETE_USER(5, false),
    GET_ALL_CLIENTS(6, true),
    GET_CLIENT_BY_ID(7, true),
    CREATE_CLIENT(8, false),
    UPDATE_CLIENT(9, false),
    DELETE_CLIENT(10, false),
    GET_CLIENTS_BY_SHORT_NAME(11, true),
    GET_CLIENTS_BY_INN(12, true),
    GET_CLIENTS_BY_KPP(13, true),
    GET_CLIENTS_BY_OGRN(14, true),

    GET_ALL_BANKS(15, true),
    GET_BANK_BY_ID(16, true),
    CREATE_BANK(17, false),
    UPDATE_BANK(18, false),
    DELETE_BANK(19, false),
    GET_ALL_ORDERS(20, true),
    GET_ORDER_BY_ID(21, true),
    GET_ORDERS_BY_USER(22, true),
    CREATE_ORDER(23, false),
    UPDATE_ORDER(24, false),
    DELETE_ORDER(25, false),
    GET_ALL_PRODUCTS(26, true),
    GET_PRODUCT_BY_ID(27, true),
    CREATE_PRODUCT(28, false),
    UPDATE_PRODUCT(29, false),
    DELETE_PRODUCT(30, false),
    ADD_PRODUCT_TO_ORDER(31, false),
    GET_PRODUCTS_BY_ORDER_ID(32, true),
    DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID(33, false),
    DELETE_PRODUCT_BY_ORDER_ID_AND_PRODUCT_ID(34, false);

    private final Integer code;

    private final boolean hasResponse;

    public Integer getCode() {
        return code;
    }

    public boolean isHasResponse() {
        return hasResponse;
    }

    MethodsCodes(Integer code, boolean hasResponse) {
        this.code = code;
        this.hasResponse = hasResponse;
    }

    public static MethodsCodes searchByCode(Integer code) {
        return Arrays.stream(MethodsCodes.values()).filter(a -> a.getCode().equals(code)).findFirst().orElse(null);
    }


//    authenticateUser
//            authorizeUser
//            getProductById
//    updateProductQuantity
//            getInventory
//    reserveProduct

//    getOrdersByClient
}
