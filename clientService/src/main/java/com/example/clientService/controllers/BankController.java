package com.example.clientService.controllers;

import com.example.clientService.dao.ClientDAO;
import com.example.clientService.dto.BankDTO;
import com.example.clientService.models.Bank;
import com.example.clientService.services.BankService;
import com.example.clientService.util.*;
import com.example.clientService.util.bankUtil.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banks")
public class BankController {

    private final BankService bankService;
    private final ModelMapperUtil modelMapper;
    private final BankDTOUniqueValidator bankDTOUniqueValidator;
    private final ClientDAO clientDAO;

    @Autowired
    public BankController(BankService bankService, ModelMapperUtil modelMapper, BankDTOUniqueValidator bankDTOUniqueValidator, ClientDAO clientDAO) {
        this.bankService = bankService;
        this.modelMapper = modelMapper;
        this.bankDTOUniqueValidator = bankDTOUniqueValidator;
        this.clientDAO = clientDAO;
    }

    @GetMapping()
    public List<BankDTO> findAll() {
        return bankService.findAll().stream().map(modelMapper::convertBankToBankDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    private BankDTO findById(@PathVariable("id") int id) {
        return modelMapper.convertBankToBankDTO(bankService.findById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> create(@PathVariable("id") int clientId
            , @RequestBody BankDTO bankDTO, BindingResult bindingResult) {
        bankDTOUniqueValidator.validate(bankDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BankNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        Bank bank = modelMapper.convertBankDTOToBank(bankDTO);
        bank.setClient(clientDAO.loadClientById(clientId));
        bankService.save(bank);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody BankDTO bankDTO, BindingResult bindingResult) {
        bankDTO.setCreated_at(bankService.findById(id).getCreated_at());
        bankDTO.setId(id);
        bankDTOUniqueValidator.validate(bankDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BankNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
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
