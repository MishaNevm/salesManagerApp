package org.example.clientService.util;



import java.util.ArrayList;
import java.util.List;

//@Component
//public class BankUniqueValidator {
//
//    private final BankRepository bankRepository;
//
//    @Autowired
//    public BankUniqueValidator(BankRepository bankRepository) {
//        this.bankRepository = bankRepository;
//    }
//
//    public ErrorResponse validate(BankDTO bankDTO) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setErrors(new ArrayList<>());
//        List<Bank> bankList = bankRepository.findByBikOrCheckingAccount(bankDTO.getBik(), bankDTO.getCheckingAccount());
//        checkCheckingAccount(bankList, bankDTO, errorResponse);
//        return errorResponse;
//    }
//
//
//    private void checkCheckingAccount(List<Bank> bankList, BankDTO bankToValidate, ErrorResponse errorResponse) {
//        for (Bank bank : bankList) {
//            if (bank.getCheckingAccount().equals(bankToValidate.getCheckingAccount()) && bank.getId() != bankToValidate.getId()) {
//                ValidationError validationError = new ValidationError();
//                validationError.setField("checkingAccount");
//                validationError.setCode("0");
//                validationError.setMessage("Банк с данным рассчетным счетом уже зарегестрирован");
//                errorResponse.getErrors().add(validationError);
//            }
//        }
//    }
//}
