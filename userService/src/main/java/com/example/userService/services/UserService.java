package com.example.userService.services;

import com.example.userService.dto.UserDTO;
import com.example.userService.dto.UserDTOResponse;
import com.example.userService.models.User;
import com.example.userService.repositoryes.UserRepository;
import com.example.userService.util.ModelMapperUtil;
import com.example.userService.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapperUtil modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public UserDTOResponse findAll() {
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setResponse(userRepository.findAll().stream().map(modelMapper::convertUserToUserDTO).toList());
        return userDTOResponse;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(int id) {
        return modelMapper.convertUserToUserDTO(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        return modelMapper.convertUserToUserDTO(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
    }

    public void save(User user) {
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void delete (int id) {
        userRepository.deleteById(id);
    }
}
