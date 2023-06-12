package com.example.Frontend.service;

import com.example.Frontend.controllers.UserController;
import com.example.Frontend.dto.UserDTO;
import com.example.Frontend.dto.UserDTOResponse;
import com.example.Frontend.dto.UserLogin;
import com.example.Frontend.security.UserLoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserLoginDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserLoginDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserLoginDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTOResponse userDTOResponse = restTemplate.getForObject(String.format(UserController.GET_USER_BY_EMAIL, username), UserDTOResponse.class);
        if (userDTOResponse == null || userDTOResponse.getResponse().get(0).getEmail() == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        UserDTO userDTO = userDTOResponse.getResponse().get(0);
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(userDTO.getEmail());
        userLogin.setPassword(userDTO.getPassword());
        return new UserLoginDetails(userLogin);
    }
}
