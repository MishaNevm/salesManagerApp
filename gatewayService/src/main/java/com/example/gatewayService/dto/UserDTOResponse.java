package com.example.gatewayService.dto;

import java.util.List;

public class UserDTOResponse implements CustomResponse<UserDTO> {
    List<UserDTO> userDTOList;


    @Override
    public List<UserDTO> getResponse() {
        return userDTOList;
    }

    @Override
    public void setResponse(List<UserDTO> response) {
        userDTOList = response;
    }
}
