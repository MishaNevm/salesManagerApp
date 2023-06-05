package com.example.inventoryService.util;

public enum MethodsCodes {
    GET_ALL_PRODUCTS(26, true),
    GET_PRODUCT_BY_ID(27, true),
    CREATE_PRODUCT(28, false),
    UPDATE_PRODUCT(29, false),
    DELETE_PRODUCT(30, false),
    ADD_PRODUCT_TO_ORDER(31, false),
    GET_PRODUCTS_BY_ORDER_ID(32, true),
    DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID(33, false),
    DELETE_PRODUCT_BY_ORDER_ID_AND_PRODUCT_ID(34, false),
    UPDATE_PRODUCT_QUANTITY_IN_ORDER(35, false);

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
}
