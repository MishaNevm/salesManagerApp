package com.example.clientService.util.bankUtil;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.models.Bank;
import com.example.clientService.repositoryes.BankRepository;
import com.example.clientService.util.ValidationError;
import com.example.clientService.util.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BankDTOUniqueValidator {

    private final BankRepository bankRepository;

    @Autowired
    public BankDTOUniqueValidator(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public void validate(BankDTO bankDTO) throws ValidationException {
        ValidationException validationException = new ValidationException();
        validationException.setErrors(new ArrayList<>());
        List<Bank> bankList = bankRepository.findByBikOrCheckingAccount(bankDTO.getBik(), bankDTO.getCheckingAccount());
        checkBik(bankList, bankDTO, validationException);
        checkCheckingAccount(bankList, bankDTO, validationException);
        if (!validationException.getErrors().isEmpty()) {
            throw validationException;
        }
    }

    private void checkBik(List<Bank> bankList, BankDTO bankToValidate, ValidationException validationException) {
        for (Bank bank : bankList) {
            if (bank.getBik().equals(bankToValidate.getBik()) && bank.getId() != bankToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("bik");
                validationError.setCode("0");
                validationError.setMessage("Банк с данным бик уже зарегестрирован");
                validationException.getErrors().add(validationError);
            }
        }
    }

    private void checkCheckingAccount(List<Bank> bankList, BankDTO bankToValidate, ValidationException validationException) {
        for (Bank bank : bankList) {
            if (bank.getCheckingAccount().equals(bankToValidate.getCheckingAccount()) && bank.getId() != bankToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("checkingAccount");
                validationError.setCode("0");
                validationError.setMessage("Банк с данным рассчетным счетом уже зарегестрирован");
                validationException.getErrors().add(validationError);
            }
        }
    }
}
