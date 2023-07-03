package org.example.clientService.util;



import java.util.ArrayList;
import java.util.List;


//public class ClientUniqueValidator {
//
//
//
//
//
//    public ErrorResponse validate(ClientDTO clientDTO) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setErrors(new ArrayList<>());
//        List<Client> clientList = clientRepository
//                .findByAnyParameter(clientDTO.getShortName(),
//                        clientDTO.getInn(),
//                        clientDTO.getKpp(),
//                        clientDTO.getOgrn());
//        checkShortName(clientList, clientDTO, errorResponse);
//        checkInn(clientList, clientDTO, errorResponse);
//        checkKpp(clientList, clientDTO, errorResponse);
//        checkOgrn(clientList, clientDTO, errorResponse);
//        return errorResponse;
//    }
//
//    private void checkShortName(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
//        for (Client client : clientList) {
//            if (client.getShortName().equals(clientToValidate.getShortName()) && client.getId() != clientToValidate.getId()) {
//                ValidationError validationError = new ValidationError();
//                validationError.setField("shortName");
//                validationError.setCode("0");
//                validationError.setMessage("Клиент с данным сокращенным наименованием уже зарегистрирован");
//                errorResponse.getErrors().add(validationError);
//                return;
//            }
//        }
//    }
//
//    private void checkInn(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
//        for (Client client : clientList) {
//            if (client.getInn().equals(clientToValidate.getInn())
//                    && client.getId() != clientToValidate.getId()) {
//                ValidationError validationError = new ValidationError();
//                validationError.setField("inn");
//                validationError.setCode("0");
//                validationError.setMessage("Клиент с данным инн уже зарегистрирован");
//                errorResponse.getErrors().add(validationError);
//                return;
//            }
//        }
//    }
//
//    //У ИП нет КПП
//    private void checkKpp(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
//        ValidationError validationError = new ValidationError();
//        validationError.setField("kpp");
//        validationError.setCode("0");
//        String kpp = clientToValidate.getKpp();
//        int clientId = clientToValidate.getId();
//        if (kpp.equals("-") && !clientToValidate.getType().equals(ClientTypes.IP)) {
//            validationError.setMessage("КПП отсутствует только у ИП");
//        }
//        if (!kpp.equals("-") && clientToValidate.getType().equals(ClientTypes.IP)) {
//            validationError.setMessage("У ИП отсутствует КПП");
//        }
//
//        for (Client client : clientList) {
//            if (client.getKpp().equals(kpp) && client.getId() != clientId) {
//                validationError.setMessage("Клиент с этим КПП уже зарегистрирован");
//                break;
//            }
//        }
//
//        if (validationError.getMessage() != null) {
//            errorResponse.getErrors().add(validationError);
//        }
//    }
//
//    private void checkOgrn(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
//        for (Client client : clientList) {
//            if (client.getOgrn().equals(clientToValidate.getOgrn())
//                    && client.getId() != clientToValidate.getId()) {
//                ValidationError validationError = new ValidationError();
//                validationError.setField("ogrn");
//                validationError.setCode("0");
//                validationError.setMessage("Клиент с данным огрн уже зарегистрирован");
//                errorResponse.getErrors().add(validationError);
//                return;
//            }
//        }
//    }
//}
