package com.example.gatewayService.util;

public enum ProductTypes {
    АКСЕСУАРЫ("Аксессуары"),
    НИВЕЛИРЫ("Нивелиры"),
    ЛАЗЕРНЫЙ_СКАНЕР ("Лазерный сканер"),
    ПРИЕМНИК("Приемник"),
    ГЕОРАДАР("Георадар"),
    ТЕОДОЛИТ("Теодолит");

    private final String value;

    ProductTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
