package com.example.clientService.controllers;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.services.BankService;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.MethodsCodes;
import com.example.clientService.util.bankUtil.BankDTOUniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
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


    @GetMapping()
    public ResponseEntity<HttpStatus> findAll() {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_ALL_BANKS, bankService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_BANK_BY_ID, bankService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public void create(@RequestBody BankDTO bankDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        bankDTOUniqueValidator.validate(bankDTO, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            bankService.save(bankDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.CREATE_BANK, errorResponse);
    }

    @PatchMapping("/{id}")
    public void update(@RequestBody BankDTO bankDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        bankDTOUniqueValidator.validate(bankDTO, errorResponse);
        if (errorResponse.getErrors().isEmpty()) {
            bankDTO.setCreatedAt(bankService.findById(bankDTO.getId()).getResponse().get(0).getCreatedAt());
            bankService.update(bankDTO);
        }
        producer.sendMessageToClientTopicResponse(MethodsCodes.UPDATE_BANK, errorResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bankService.findById(id);
        bankService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
