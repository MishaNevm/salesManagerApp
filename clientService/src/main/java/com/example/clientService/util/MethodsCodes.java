package com.example.clientService.util;

import java.util.Arrays;

public enum MethodsCodes {
    GET_ALL_CLIENTS(6, true),
    GET_CLIENT_BY_ID(7, true),
    CREATE_CLIENT(8, false),
    UPDATE_CLIENT(9, false),
    DELETE_CLIENT(10, false);
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
}
