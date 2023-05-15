package com.example.userService.dto;

import java.util.List;

public class UserDTOResponse {
    List<UserDTO> userDTOList;

    public List<UserDTO> getResponse() {
        return userDTOList;
    }

    public void setResponse(List<UserDTO> response) {
        userDTOList = response;
    }
}