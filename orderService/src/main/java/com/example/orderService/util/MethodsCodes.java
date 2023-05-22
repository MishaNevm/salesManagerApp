package com.example.orderService.util;

public enum MethodsCodes {
    GET_ALL_ORDERS(20, true),
    GET_ORDER_BY_ID(21, true),
    GET_ORDERS_BY_USER(22, true),
    CREATE_ORDER(23, false),
    UPDATE_ORDER(24, false),
    DELETE_ORDER(25, false);

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
