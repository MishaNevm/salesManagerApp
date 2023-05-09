package com.example.clientService.util.bankUtil;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.models.Bank;
import com.example.clientService.repositoryes.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class BankDTOUniqueValidator implements Validator {

    private final BankRepository bankRepository;

    @Autowired
    public BankDTOUniqueValidator(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(BankDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankDTO bankDTO = (BankDTO) target;
        checkBik(bankRepository.findByBik(bankDTO.getBik()), bankDTO, errors);
        checkCheckingAccount(bankRepository.findByCheckingAccount(bankDTO.getCheckingAccount()), bankDTO, errors);
    }

    private void checkBik(Optional<Bank> bankFromDb, BankDTO bankToValidate, Errors errors) {
        if (bankFromDb.isPresent() && bankFromDb.get().getId() != bankToValidate.getId()) {
            errors.rejectValue("bik", "0", "Банк с данным бик уже зарегистрирован");
        }
    }

    private void checkCheckingAccount(Optional<Bank> bankFromDb, BankDTO bankToValidate, Errors errors) {
        if (bankFromDb.isPresent() && bankFromDb.get().getId() != bankToValidate.getId()) {
            errors.rejectValue("checkingAccount", "0", "Банк с данным рассчетным счетом уже зарегистрирован");
        }
    }
}
