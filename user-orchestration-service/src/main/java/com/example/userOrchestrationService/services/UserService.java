package com.example.userOrchestrationService.services;

import com.example.userOrchestrationService.dto.UserDTO;
import com.example.userOrchestrationService.dto.UserDTOResponse;
import com.example.userOrchestrationService.models.User;
import com.example.userOrchestrationService.repositoryes.UserRepository;
import com.example.userOrchestrationService.util.ModelMapperUtil;
import com.example.userOrchestrationService.util.UserUtil.UserNotFoundException;
import com.example.userOrchestrationService.util.UserUtil.UserRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtil modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final String adminPass;

    public UserService(UserRepository userRepository, ModelMapperUtil modelMapper, PasswordEncoder passwordEncoder, @Value("${admin-pass}") String adminPass) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminPass = adminPass;

        initializeAdminUser();
    }

    private void initializeAdminUser() {
        Optional<User> userOptional = userRepository.findByEmail("admin");
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setEmail("admin");
            user.setPassword(passwordEncoder.encode(adminPass));
            user.setRole(UserRoles.ROLE_ADMIN);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public UserDTOResponse findAll() {
        List<UserDTO> userDTOs = userRepository.findAll()
                .stream()
                .map(modelMapper::convertUserToUserDTO)
                .collect(Collectors.toList());
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setResponse(userDTOs);
        return userDTOResponse;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.convertUserToUserDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTOResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(new User());
        UserDTO userDTO = modelMapper.convertUserToUserDTO(user);
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setResponse(Collections.singletonList(userDTO));
        return userDTOResponse;
    }

    public void save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = modelMapper.convertUserDTOToUser(userDTO);
        user.setCreatedAt(new Date());
        userRepository.save(user);
    }

    public void update(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(UserNotFoundException::new);

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setCreatedAt(existingUser.getCreatedAt());

        User updatedUser = modelMapper.convertUserDTOToUser(userDTO);
        userRepository.save(updatedUser);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
