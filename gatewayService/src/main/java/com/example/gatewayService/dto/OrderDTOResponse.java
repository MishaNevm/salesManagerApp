package com.example.gatewayService.dto;

import com.example.gatewayService.util.CustomResponse;

import java.util.List;

public class OrderDTOResponse implements CustomResponse<OrderDTO> {

    private List<OrderDTO> orderDTOList;

    @Override
    public List<OrderDTO> getResponse() {
        return orderDTOList;
    }

    @Override
    public void setResponse(List<OrderDTO> response) {
        orderDTOList = response;
    }
}
