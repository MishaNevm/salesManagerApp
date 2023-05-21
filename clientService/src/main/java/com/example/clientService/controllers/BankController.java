package com.example.clientService.controllers;

import com.example.clientService.dao.ClientDAO;
import com.example.clientService.dto.BankDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.models.Bank;
import com.example.clientService.services.BankService;
import com.example.clientService.util.*;
import com.example.clientService.util.bankUtil.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
public class BankController {

    private final Producer producer;
    private final BankService bankService;
    private final ClientDAO clientDAO;
    private final ModelMapperUtil modelMapper;

    @Autowired
    public BankController(Producer producer, BankService bankService, ClientDAO clientDAO, ModelMapperUtil modelMapper) {
        this.producer = producer;
        this.bankService = bankService;
        this.clientDAO = clientDAO;
        this.modelMapper = modelMapper;
    }


    @GetMapping()
    public ResponseEntity<HttpStatus> findAll() {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_ALL_BANKS.getCode(), bankService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_BANK_BY_ID.getCode(), bankService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> create(@PathVariable("id") int clientId
            , @RequestBody BankDTO bankDTO) {
//        if (bindingResult.hasErrors()) {
//            throw new BankNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        Bank bank = modelMapper.convertBankDTOToBank(bankDTO);
        bank.setClient(clientDAO.loadClientById(clientId));
        bankService.save(bank);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody BankDTO bankDTO) {
        bankDTO.setCreated_at(bankService.findById(bankDTO.getId()).getResponse().get(0).getCreated_at());
//        if (bindingResult.hasErrors()) {
//            throw new BankNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        bankService.update(modelMapper.convertBankDTOToBank(bankDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bankService.findById(id);
        bankService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandle(BankNotSaveException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandle(BankNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Данный банк не найден"), HttpStatus.BAD_REQUEST);
    }
}
