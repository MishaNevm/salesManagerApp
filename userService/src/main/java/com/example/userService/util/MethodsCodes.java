package com.example.userService.util;

import java.util.Arrays;

public enum MethodsCodes {

    GET_ALL_USERS(0, true),
    GET_USER_BY_ID(1, true),
    GET_USER_BY_EMAIL(2, true),
    CREATE_USER(3, false),
    UPDATE_USER(4, false),
    DELETE_USER(5, false);

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
