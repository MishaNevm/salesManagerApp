package com.example.gatewayService.dto;

import java.util.List;

public class BankDTOResponse implements CustomResponse<BankDTO> {

    private List<BankDTO> bankDTOList;

    @Override
    public List<BankDTO> getResponse() {
        return bankDTOList;
    }

    @Override
    public void setResponse(List<BankDTO> response) {
        bankDTOList = response;
    }
}
