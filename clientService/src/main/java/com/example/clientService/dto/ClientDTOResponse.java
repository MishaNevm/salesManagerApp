package com.example.clientService.dto;

import java.util.List;

public class ClientDTOResponse implements CustomResponse<ClientDTO> {
    private List<ClientDTO> clientDTOList;


    @Override
    public List<ClientDTO> getResponse() {
        return clientDTOList;
    }

    @Override
    public void setResponse(List<ClientDTO> response) {
        clientDTOList = response;
    }
}
