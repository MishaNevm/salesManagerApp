package com.example.clientService.util.clientUtil;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.models.Client;
import com.example.clientService.repositoryes.ClientRepository;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ClientDTOUniqueValidator {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientDTOUniqueValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void validate(ClientDTO clientDTO, ErrorResponse errorResponse) {
        errorResponse.setErrors(new ArrayList<>());
        List<Client> clientList = clientRepository
                .findByAnyParameter(clientDTO.getShortName(),
                        clientDTO.getInn(),
                        clientDTO.getKpp(),
                        clientDTO.getOgrn());
        checkShortName(clientList, clientDTO, errorResponse);
        checkInn(clientList, clientDTO, errorResponse);
        checkKpp(clientList, clientDTO, errorResponse);
        checkOgrn(clientList, clientDTO, errorResponse);
    }

    private void checkShortName(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
        for (Client client : clientList) {
            if (client.getShortName().equals(clientToValidate.getShortName()) && client.getId() != clientToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("shortName");
                validationError.setCode("0");
                validationError.setMessage("Клиент с данным сокращенным наименованием уже зарегистрирован");
                errorResponse.getErrors().add(validationError);
                return;
            }
        }
    }

    private void checkInn(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
        for (Client client : clientList) {
            if (client.getInn().equals(clientToValidate.getInn())
                    && client.getId() != clientToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("inn");
                validationError.setCode("0");
                validationError.setMessage("Клиент с данным инн уже зарегистрирован");
                errorResponse.getErrors().add(validationError);
                return;
            }
        }
    }

    //У ИП нет КПП
    private void checkKpp(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
        ValidationError validationError = new ValidationError();
        validationError.setField("kpp");
        validationError.setCode("0");
        for (Client client : clientList) {
            if (clientToValidate.getKpp().equals("-") && !clientToValidate.getType().equals(ClientTypes.IP)) {
                validationError.setMessage("КПП отсутствует только у ИП");
                return;
            }
            if (!clientToValidate.getKpp().equals("-")) {
                if (clientToValidate.getType().equals(ClientTypes.IP)) {
                    validationError.setMessage("У ИП отсутсвует КПП");
                } else if (client.getKpp().equals(clientToValidate.getKpp())
                        && client.getId() != clientToValidate.getId()) {
                    validationError.setMessage("Клиент с этим КПП уже зарегистрирован");
                }
            }
        }
        if (validationError.getMessage() != null) {
            errorResponse.getErrors().add(validationError);
        }
    }

    private void checkOgrn(List<Client> clientList, ClientDTO clientToValidate, ErrorResponse errorResponse) {
        for (Client client : clientList) {
            if (client.getOgrn().equals(clientToValidate.getOgrn())
                    && client.getId() != clientToValidate.getId()) {
                ValidationError validationError = new ValidationError();
                validationError.setField("ogrn");
                validationError.setCode("0");
                validationError.setMessage("Клиент с данным огрн уже зарегистрирован");
                errorResponse.getErrors().add(validationError);
                return;
            }
        }
    }
}
