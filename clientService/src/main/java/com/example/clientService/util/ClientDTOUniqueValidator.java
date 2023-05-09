package com.example.clientService.util;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.models.Client;
import com.example.clientService.repositoryes.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class ClientDTOUniqueValidator implements Validator {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientDTOUniqueValidator(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ClientDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClientDTO clientToValidate = (ClientDTO) target;
        checkKpp(clientToValidate, errors);
        checkShortName(clientToValidate, errors);
        checkInn(clientToValidate, errors);
        checkOgrn(clientToValidate, errors);
    }

    private void checkShortName(ClientDTO clientToValidate, Errors errors) {
        if (clientToValidate.getShortName() != null) {
            Optional<Client> client = clientRepository.findByShortName(clientToValidate.getShortName());
            if (client.isPresent()
                    && client.get().getId() != clientToValidate.getId()) {
                errors.rejectValue("shortName", "0", "Клиент с этим именем уже существует");
            }
        }
    }

    private void checkInn(ClientDTO clientToValidate, Errors errors) {
        if (clientToValidate.getInn() != null) {
            Optional<Client> client = clientRepository.findByInn(clientToValidate.getInn());
            if (client.isPresent()
                    && client.get().getId() != clientToValidate.getId()) {
                errors.rejectValue("inn", "0", "Клиент с этим ИНН уже cуществует");
            }
        }
    }

    //У ИП нет КПП
    private void checkKpp(ClientDTO clientToValidate, Errors errors) {
        if (clientToValidate.getType() != null) {
            Optional<Client> client = clientRepository.findByKpp(clientToValidate.getKpp());
            if (clientToValidate.getKpp().equals("-") && !clientToValidate.getType().equals(ClientTypes.IP)) {
                errors.rejectValue("kpp", "0", "КПП отсутствует только у ИП");
                return;
            }
            if (!clientToValidate.getKpp().equals("-")) {
                if (clientToValidate.getType().equals(ClientTypes.IP)) {
                    errors.rejectValue("kpp", "0", "У ИП отсутсвует КПП");
                } else if (client.isPresent()
                        && client.get().getId() != clientToValidate.getId()) {
                    errors.rejectValue("kpp", "0", "Клиент с этим КПП уже зарегистрирован");
                }
            }
        }
    }

    private void checkOgrn(ClientDTO clientToValidate, Errors errors) {
        if (clientToValidate.getOgrn() != null) {
            Optional<Client> client = clientRepository.findByOgrn(clientToValidate.getOgrn());
            if (client.isPresent()
                    && client.get().getId() != clientToValidate.getId()) {
                errors.rejectValue("ogrn", "0", "Клиент с этим ОГРН уже cуществует");
            }
        }
    }
}
