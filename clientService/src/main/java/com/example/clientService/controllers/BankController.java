package com.example.clientService.controllers;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.services.BankService;
import com.example.clientService.util.*;
import com.example.clientService.util.bankUtil.BankDTOUniqueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public ResponseEntity<HttpStatus> create(@RequestBody BankDTO bankDTO) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(bankDTO, "bank");
        bankDTOUniqueValidator.validate(bankDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            bankService.save(bankDTO);
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setFieldErrorList(new ArrayList<>());
        bindingResult.getFieldErrors().forEach(a -> {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setField(a.getField());
            errorMessage.setMessage(a.getDefaultMessage());
            errorMessage.setCode(a.getCode());
            errorResponse.getFieldErrorList().add(errorMessage);
        });
        producer.sendMessageToClientTopicResponse(MethodsCodes.CREATE_BANK, errorResponse);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody BankDTO bankDTO) {
        bankDTO.setCreatedAt(bankService.findById(bankDTO.getId()).getResponse().get(0).getCreatedAt());
        bankService.update(bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bankService.findById(id);
        bankService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
