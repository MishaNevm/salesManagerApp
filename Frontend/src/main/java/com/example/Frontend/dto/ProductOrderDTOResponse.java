package com.example.Frontend.dto;



import com.example.Frontend.util.CustomResponse;

import java.util.List;

public class ProductOrderDTOResponse implements CustomResponse<ProductOrderDTO> {

    private List<ProductOrderDTO> productOrderDTOList;

    @Override
    public List<ProductOrderDTO> getResponse() {
        return productOrderDTOList;
    }

    @Override
    public void setResponse(List<ProductOrderDTO> response) {
        productOrderDTOList = response;
    }
}
