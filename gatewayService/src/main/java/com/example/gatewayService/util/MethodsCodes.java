package com.example.gatewayService.util;

import java.util.Arrays;

public enum MethodsCodes {

    GET_ALL_USERS(0, true, false),
    GET_USER_BY_ID(1, true, false),
    GET_USER_BY_EMAIL(2, true, false),
    CREATE_USER(3, false, true),
    UPDATE_USER(4, false, true),
    DELETE_USER(5, false, false),
    GET_ALL_CLIENTS(6, true, false),
    GET_CLIENT_BY_ID(7, true, false),
    CREATE_CLIENT(8, false, true),
    UPDATE_CLIENT(9, false, true),
    DELETE_CLIENT(10, false, false),
    GET_CLIENTS_BY_SHORT_NAME(11, true, false),
    GET_CLIENTS_BY_INN(12, true, false),
    GET_CLIENTS_BY_KPP(13, true, false),
    GET_CLIENTS_BY_OGRN(14, true, false),
    GET_ALL_BANKS(15, true, false),
    GET_BANK_BY_ID(16, true, false),
    CREATE_BANK(17, false, true),
    UPDATE_BANK(18, false, true),
    DELETE_BANK(19, false, false),
    GET_ALL_ORDERS(20, true, false),
    GET_ORDER_BY_ID(21, true, false),
    GET_ORDERS_BY_USER(22, true, false),
    CREATE_ORDER(23, false, true),
    UPDATE_ORDER(24, false, true),
    DELETE_ORDER(25, false, false),
    GET_ALL_PRODUCTS(26, true, false),
    GET_PRODUCT_BY_ID(27, true, false),
    CREATE_PRODUCT(28, false, true),
    UPDATE_PRODUCT(29, false, true),
    DELETE_PRODUCT(30, false, false),
    ADD_PRODUCT_TO_ORDER(31, false, false),
    GET_PRODUCTS_BY_ORDER_ID(32, true, false),
    DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID(33, false, false),
    DELETE_PRODUCT_BY_ORDER_ID_AND_PRODUCT_ID(34, false, false),
    UPDATE_PRODUCT_QUANTITY_IN_ORDER(35, false, false),
    GET_ORDERS_BY_CLIENT_ID(36, true, false);
    private final Integer code;
    private final boolean hasModelResponse;
    private final boolean hasErrorResponse;
    public Integer getCode() {
        return code;
    }

    public boolean isHasModelResponse() {
        return hasModelResponse;
    }

    public boolean isHasErrorResponse() {
        return hasErrorResponse;
    }

    MethodsCodes(Integer code, boolean hasModelResponse, boolean hasErrorResponse) {
        this.code = code;
        this.hasModelResponse = hasModelResponse;
        this.hasErrorResponse = hasErrorResponse;
    }

    public static MethodsCodes searchByCode(Integer code) {
        return Arrays.stream(MethodsCodes.values()).filter(a -> a.getCode().equals(code)).findFirst().orElse(null);
    }
}
