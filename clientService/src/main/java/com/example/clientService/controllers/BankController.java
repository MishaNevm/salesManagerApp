package com.example.clientService.controllers;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.services.BankService;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.MethodsCodes;
import com.example.clientService.util.bankUtil.BankDTOUniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankController {

    private final Producer producer;
    private final BankService bankService;
    private final BankDTOUniqueValidator bankDTOUniqueValidator;


    @Autowired
    public BankController(Producer producer, BankService bankService, BankDTOUniqueValidator bankDTOUniqueValidator) {
        this.producer = producer;
        this.bankService = bankService;
        this.bankDTOUniqueValidator = bankDTOUniqueValidator;
    }

    public void findAll() {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_ALL_BANKS, bankService.findAll());
    }

    public void findById(int id) {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_BANK_BY_ID, bankService.findById(id));
    }

    public void create(BankDTO bankDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        bankDTOUniqueValidator.validate(bankDTO, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            bankService.save(bankDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.CREATE_BANK, errorResponse);
    }

    public void update(BankDTO bankDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        bankDTOUniqueValidator.validate(bankDTO, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            bankDTO.setCreatedAt(bankService.findById(bankDTO.getId()).getResponse().get(0).getCreatedAt());
            bankService.update(bankDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.UPDATE_BANK, errorResponse);
    }

    public void delete(int id) {
        bankService.findById(id);
        bankService.delete(id);
    }
}
