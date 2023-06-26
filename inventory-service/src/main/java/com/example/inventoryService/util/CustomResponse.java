package com.example.inventoryService.util;

import java.util.List;

public abstract class CustomResponse<T> {

    private List<T> response;

    public List<T> getResponse() {
        return response;
    }

    public void setResponse(List<T> response) {
        this.response = response;
    }
}
