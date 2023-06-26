package com.example.frontend.util;

public enum Urls {

    LOGIN_URL ("http://localhost:8484/api/v1/auth/login"),
    GET_ALL_USERS ("http://localhost:8484/api/v1/users"),
    GET_USER_BY_EMAIL("http://localhost:8484/api/v1/users?email=%s"),
    GET_USER_BY_ID("http://localhost:8484/api/v1/users/%d"),

    CREATE_USER(GET_ALL_USERS.url),
    UPDATE_USER(GET_USER_BY_ID.url),
    DELETE_USER(GET_USER_BY_ID.url),

    GET_ALL_PRODUCTS("http://localhost:8484/api/v1/products"),
    GET_PRODUCT_BY_ID ("http://localhost:8484/api/v1/products/%d"),
    CREATE_PRODUCT (GET_ALL_PRODUCTS.url),
    UPDATE_PRODUCT (GET_PRODUCT_BY_ID.url),
    DELETE_PRODUCT (GET_PRODUCT_BY_ID.url),
    GET_ALL_ORDERS ("http://localhost:8484/api/v1/orders"),

    GET_ORDER_BY_ID ("http://localhost:8484/api/v1/orders/%d"),
    GET_PRODUCTS_BY_ORDER_ID("http://localhost:8484/api/v1/products/get-products-by-order?order-id=%d"),
    CREATE_ORDER (GET_ALL_ORDERS.url),
    ADD_PRODUCT_TO_ORDER("http://localhost:8484/api/v1/orders/%d/add-product?product-id=%d&quantity=%d"),
    UPDATE_ORDER (GET_ORDER_BY_ID.url),
    DELETE_ORDER (GET_ORDER_BY_ID.url),
    DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID("http://localhost:8484/api/v1/orders/%d/delete-products"),
    DELETE_PRODUCT_IN_ORDER_BY_ORDER_ID_AND_PRODUCT_ID("http://localhost:8484/api/v1/orders/%d/delete-from-order?product-id=%d"),
    UPDATE_PRODUCT_QUANTITY_IN_ORDER("http://localhost:8484/api/v1/orders/%d/update-product-quantity?product-id=%d&quantity=%d"),
    GET_ORDERS_BY_CLIENT_SHORT_NAME("http://localhost:8484/api/v1/orders?client_short_name=%s"),

    GET_ALL_CLIENTS("http://localhost:8484/api/v1/clients"),
    GET_CLIENT_BY_ID("http://localhost:8484/api/v1/clients/%d"),
    CREATE_CLIENT (GET_ALL_CLIENTS.url),
    CREATE_BANK_TO_CLIENT (GET_CLIENT_BY_ID.url),
    UPDATE_CLIENT (GET_CLIENT_BY_ID.url),
    DELETE_CLIENT (GET_CLIENT_BY_ID.url),

    GET_ALL_BANKS("http://localhost:8484/api/v1/banks"),
    GET_BANK_BY_ID("http://localhost:8484/api/v1/banks/%d"),
    UPDATE_BANK (GET_BANK_BY_ID.url),
    DELETE_BANK (GET_BANK_BY_ID.url);

    private final String url;

    Urls(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
