package com.example.clientService.dto;

import java.util.List;

public interface CustomResponse<T> {
    List<T> getResponse();

    void setResponse(List<T> response);
}
