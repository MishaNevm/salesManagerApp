package com.example.Frontend.dto;



import com.example.Frontend.util.CustomResponse;

import java.util.List;

public class ProductDTOResponse implements CustomResponse<ProductDTO> {

    private List<ProductDTO> productDTOList;

    @Override
    public List<ProductDTO> getResponse() {
        return productDTOList;
    }

    @Override
    public void setResponse(List<ProductDTO> response) {
        productDTOList = response;
    }
}
