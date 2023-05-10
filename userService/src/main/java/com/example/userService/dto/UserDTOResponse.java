package com.example.userService.dto;

import java.util.List;

public class UserDTOResponse {
    List<UserDTO> userDTOList;

    public List<UserDTO> getUserDTOList() {
        return userDTOList;
    }

    public void setUserDTOList(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }
}
