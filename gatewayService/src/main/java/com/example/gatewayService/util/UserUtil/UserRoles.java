package com.example.gatewayService.util.UserUtil;

public enum UserRoles {
    ADMIN("Администратор"),
    MANAGER ("Менеджер"),
    SUPERVISOR ("Сепервайзер"),
    DIRECTOR ("Директор");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
