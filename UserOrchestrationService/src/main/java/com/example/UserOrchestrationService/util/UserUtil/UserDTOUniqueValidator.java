package com.example.UserOrchestrationService.util.UserUtil;


import com.example.UserOrchestrationService.dto.UserDTO;
import com.example.UserOrchestrationService.models.User;
import com.example.UserOrchestrationService.repositoryes.UserRepository;
import com.example.UserOrchestrationService.util.ErrorResponse;
import com.example.UserOrchestrationService.util.ErrorResponseException;
import com.example.UserOrchestrationService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class UserDTOUniqueValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserDTOUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserDTO userDTO) {
        ErrorResponse errorResponse = checkEmail(userDTO);
        if (errorResponse.getErrors() != null) {
            throw new ErrorResponseException(errorResponse);
        }
    }

    private ErrorResponse checkEmail(UserDTO userToValidate) {
        String email = userToValidate.getEmail();
        Optional<User> userFromDB = userRepository.findByEmail(email);

        if (userFromDB.isPresent() && userFromDB.get().getId() != userToValidate.getId()) {
            ValidationError validationError = new ValidationError();
            validationError.setField("email");
            validationError.setCode("0");
            validationError.setMessage("Пользователь с данным email уже зарегистрирован");

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrors(Collections.singletonList(validationError));

            return errorResponse;
        }

        return new ErrorResponse();
    }

}
