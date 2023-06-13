package com.example.gatewayService.util.UserUtil;


import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.models.User;
import com.example.gatewayService.repositoryes.UserRepository;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserDTOUniqueValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserDTOUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserDTO userDTO, ErrorResponse errorResponse) {
        errorResponse.setErrors(new ArrayList<>());
        checkEmail(userDTO, errorResponse);
    }

    private void checkEmail(UserDTO userToValidate, ErrorResponse errorResponse) {
        Optional<User> userFromDB = userRepository.findByEmail(userToValidate.getEmail());
        if (userFromDB.isPresent() && userFromDB.get().getId() != userToValidate.getId()) {
            ValidationError validationError = new ValidationError();
            validationError.setField("email");
            validationError.setCode("0");
            validationError.setMessage("Пользователь с данным email уже зарегистрирован");
            errorResponse.getErrors().add(validationError);
        }
    }
}
