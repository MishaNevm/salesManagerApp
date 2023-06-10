package com.example.Frontend.dto;


import com.example.Frontend.util.CustomResponse;

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
