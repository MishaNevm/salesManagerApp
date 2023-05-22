package com.example.inventoryService.dto;

import java.util.List;

public interface CustomResponse<T> {
    List<T> getResponse();

    void setResponse(List<T> response);
}
