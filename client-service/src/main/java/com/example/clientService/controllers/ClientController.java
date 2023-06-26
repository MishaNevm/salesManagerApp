package com.example.clientService.controllers;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.services.ClientService;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.MethodsCodes;
import com.example.clientService.util.clientUtil.ClientDTOUniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientController {

    private final ClientService clientService;
    private final ClientDTOUniqueValidator clientDTOUniqueValidator;
    private final Producer producer;


    @Autowired
    public ClientController(ClientService clientService,
                            ClientDTOUniqueValidator clientDTOUniqueValidator, Producer producer) {
        this.clientService = clientService;
        this.clientDTOUniqueValidator = clientDTOUniqueValidator;
        this.producer = producer;
    }

    public void findAll() {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_ALL_CLIENTS, clientService.findAll());
    }

    public void findById(int id) {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_CLIENT_BY_ID, clientService.findById(id));
    }

    public void create(ClientDTO clientDTO) {
        ErrorResponse errorResponse = clientDTOUniqueValidator.validate(clientDTO);
        if (errorResponse.getErrors().isEmpty()) {
            clientService.save(clientDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.CREATE_CLIENT, errorResponse);
    }

    public void update(ClientDTO clientDTO) {
        ErrorResponse errorResponse = clientDTOUniqueValidator.validate(clientDTO);
        if (errorResponse.getErrors().isEmpty()) {
            clientService.update(clientDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.UPDATE_CLIENT, errorResponse);
    }

    public void delete(int id) {
        clientService.delete(id);
    }
}
