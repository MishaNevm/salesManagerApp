package com.example.UserOrchestrationService.controllers;


import com.example.UserOrchestrationService.dto.BankDTO;
import com.example.UserOrchestrationService.dto.BankDTOResponse;
import com.example.UserOrchestrationService.kafka.Consumer;
import com.example.UserOrchestrationService.kafka.Producer;
import com.example.UserOrchestrationService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/banks")
public class BankController {
    private final Producer producer;
    private final Consumer consumer;
    private final GlobalExceptionHandler globalExceptionHandler;

    public BankController(Producer producer, Consumer consumer, GlobalExceptionHandler globalExceptionHandler) {
        this.producer = producer;
        this.consumer = consumer;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping
    public BankDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_BANKS, new BankDTO());
        return waitForResponse(MethodsCodes.GET_ALL_BANKS);
    }

    @GetMapping("/{id}")
    public BankDTO findById(@PathVariable("id") int id) throws InterruptedException {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_BANK_BY_ID, bankDTO);
        return waitForResponse(MethodsCodes.GET_BANK_BY_ID).getResponse().get(0);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody BankDTO bankDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_BANK, bankDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.UPDATE_BANK);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_BANK, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private BankDTOResponse waitForResponse(MethodsCodes methodCode) throws InterruptedException {
        return (BankDTOResponse) consumer.getResponseMap().get(methodCode).poll(15, TimeUnit.SECONDS);
    }
}
