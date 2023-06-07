package com.example.clientService.util;

public enum MethodsCodes {
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
    DELETE_BANK(19, false);
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
