package org.example.clientService.util;


import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.dao.BankDAO;
import org.example.clientService.models.Bank;

public class BankUniqueValidator {

    private final BankDAO bankDAO;

    public BankUniqueValidator() {
        bankDAO = BankDAO.getInstance();
    }

    public void validate(Bank bank, BankServiceOuterClass.ErrorResponse.Builder errorResponseBuilder) {
        BankServiceOuterClass.ValidationError.Builder validationErrorBuilder = BankServiceOuterClass.ValidationError.newBuilder();
        Bank bankFromDb = bankDAO.findByCheckingAccount(bank.getCheckingAccount());
        checkCheckingAccount(bankFromDb, bank, validationErrorBuilder);
        if (validationErrorBuilder.isInitialized()) {
            errorResponseBuilder.addErrors(validationErrorBuilder.build());
        }
    }


    private void checkCheckingAccount(Bank bankFromDb, Bank bank, BankServiceOuterClass.ValidationError.Builder validationErrorBuilder) {
        if (bank.getCheckingAccount().equals(bankFromDb.getCheckingAccount()) && bank.getId() != bankFromDb.getId()) {
            validationErrorBuilder.setField("checkingAccount");
            validationErrorBuilder.setCode("0");
            validationErrorBuilder.setMessage("Банк с данным рассчетным счетом уже зарегестрирован");
        }
    }
}
