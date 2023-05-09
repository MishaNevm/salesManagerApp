package com.example.userService.util;

import com.example.userService.dto.UserDTO;
import com.example.userService.models.User;
import com.example.userService.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserDTOUniqueValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserDTOUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        Optional<User> userFromDB = userRepository.findByEmail(userDTO.getEmail());
        if (userFromDB.isPresent() && userFromDB.get().getId() != userDTO.getId()) {
            errors.rejectValue("email", "0", "Данный пользователь уже зарегистрован");
        }
    }
}
