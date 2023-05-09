package com.example.bankService.controllers;

import com.example.bankService.dto.BankDTO;
import com.example.bankService.models.Bank;
import com.example.bankService.services.BankService;
import com.example.bankService.util.ErrorResponse;
import com.example.bankService.util.ModelMapperUtil;
import com.example.bankService.util.BankDTOUniqueValidator;
import com.example.bankService.util.BankNotFoundException;
import com.example.bankService.util.BankNotSaveException;
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

    @Autowired
    public BankController(BankService bankService, ModelMapperUtil modelMapper, BankDTOUniqueValidator bankDTOUniqueValidator) {
        this.bankService = bankService;
        this.modelMapper = modelMapper;
        this.bankDTOUniqueValidator = bankDTOUniqueValidator;
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
        bank.setClient(clientId);
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
