package com.example.gatewayService.services;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.models.User;
import com.example.gatewayService.repositoryes.UserRepository;
;
import com.example.gatewayService.util.ModelMapperUtil;
import com.example.gatewayService.util.UserUtil.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapperUtil modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserDTOResponse findAll() {
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setResponse(userRepository.findAll().stream().map(modelMapper::convertUserToUserDTO).toList());
        return userDTOResponse;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(int id) {
        return modelMapper
                .convertUserToUserDTO(userRepository
                        .findById(id)
                        .orElseThrow(UserNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public UserDTOResponse findByEmail(String email) {
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse
                .setResponse(Collections
                        .singletonList(modelMapper
                                .convertUserToUserDTO(userRepository
                                        .findByEmail(email)
                                        .orElse(new User()))));
        return userDTOResponse;
    }

    public void save(UserDTO userDTO) {
        User user = modelMapper.convertUserDTOToUser(userDTO);
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    public void update(UserDTO userDTO) {
        userDTO.setCreatedAt(userRepository.findById(userDTO.getId()).orElseThrow(UserNotFoundException::new).getCreatedAt());
        userRepository.save(modelMapper.convertUserDTOToUser(userDTO));
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
