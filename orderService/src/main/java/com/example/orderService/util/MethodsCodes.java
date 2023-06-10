package com.example.orderService.util;

public enum MethodsCodes {
    GET_ALL_ORDERS(20, true),
    GET_ORDER_BY_ID(21, true),
    GET_ORDERS_BY_USER(22, true),
    CREATE_ORDER(23, false),
    UPDATE_ORDER(24, false),
    GET_ORDERS_BY_CLIENT_ID(36, true);

    private final Integer code;

    private final boolean hasModelResponse;

    public Integer getCode() {
        return code;
    }

    public boolean isHasModelResponse() {
        return hasModelResponse;
    }

    MethodsCodes(Integer code, boolean hasModelResponse) {
        this.code = code;
        this.hasModelResponse = hasModelResponse;
    }
}
