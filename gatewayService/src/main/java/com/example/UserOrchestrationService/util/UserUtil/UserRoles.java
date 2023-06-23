package com.example.UserOrchestrationService.util.UserUtil;

public enum UserRoles {
    ROLE_ADMIN("Администратор"),
    ROLE_MANAGER ("Менеджер"),
    ROLE_SUPERVISOR ("Сепервайзер"),
    ROLE_DIRECTOR ("Директор");

    private final String value;

    UserRoles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
