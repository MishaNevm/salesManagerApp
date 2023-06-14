package com.example.clientService.util.clientUtil;

public enum ClientTypes {
    IP("ИП"),
    OOO("ООО"),
    AO("АО"),
    ZAO("ЗАО"),
    NKO("НКО"),
    PAO("ПАО"),
    KFH("КФХ");

    private final String value;

    ClientTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
