package com.example.userOrchestrationService.util.UserUtil;


import com.example.userOrchestrationService.dto.UserDTO;
import com.example.userOrchestrationService.models.User;
import com.example.userOrchestrationService.repositoryes.UserRepository;
import com.example.userOrchestrationService.util.ErrorResponse;
import com.example.userOrchestrationService.util.ErrorResponseException;
import com.example.userOrchestrationService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
