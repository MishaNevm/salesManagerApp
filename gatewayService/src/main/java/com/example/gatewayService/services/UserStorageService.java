package com.example.gatewayService.services;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import org.springframework.stereotype.Service;

@Service
public class UserStorageService {

    private UserDTOResponse userDTO;

    public UserDTOResponse getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTOResponse userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getOneUser() {
        return userDTO.getUserDTOList().get(0);
    }
}

