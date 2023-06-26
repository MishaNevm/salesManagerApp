package com.example.clientService.util.bankUtil;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.models.Bank;
import com.example.clientService.repositoryes.BankRepository;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankDTOUniqueValidator {

    private final BankRepository bankRepository;

    @Autowired
    public BankDTOUniqueValidator(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public ErrorResponse validate(BankDTO bankDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ArrayList<>());
        List<Bank> bankList = bankRepository.findByBikOrCheckingAccount(bankDTO.getBik(), bankDTO.getCheckingAccount());
        checkBik(bankList, bankDTO, errorResponse);
        checkCheckingAccount(bankList, bankDTO, errorResponse);
        return errorResponse;
    }

    private void checkBik(List<Bank> bankList, BankDTO bankToValidate, ErrorResponse errorResponse) {
        for (Bank bank : bankList) {
            if (bank.getBik().equals(bankToValidate.getBik()) && bank.getId() != bankToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("bik");
                validationError.setCode("0");
                validationError.setMessage("Банк с данным бик уже зарегестрирован");
                errorResponse.getErrors().add(validationError);
            }
        }
    }

    private void checkCheckingAccount(List<Bank> bankList, BankDTO bankToValidate, ErrorResponse errorResponse) {
        for (Bank bank : bankList) {
            if (bank.getCheckingAccount().equals(bankToValidate.getCheckingAccount()) && bank.getId() != bankToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("checkingAccount");
                validationError.setCode("0");
                validationError.setMessage("Банк с данным рассчетным счетом уже зарегестрирован");
                errorResponse.getErrors().add(validationError);
            }
        }
    }
}
