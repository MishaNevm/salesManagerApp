package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.BankDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/banks")
public class BankController {
    private final Producer producer;
    private final Consumer consumer;

    private BankDTO bankDTO;

    public BankController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }


    @GetMapping()
    public BankDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_BANKS, new BankDTO());
        return (BankDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_BANKS).poll(15, TimeUnit.SECONDS);
    }

    @GetMapping("/{id}")
    private BankDTO findById(@PathVariable("id") int id) throws InterruptedException {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_BANK_BY_ID, bankDTO);
        return (BankDTO) Objects.requireNonNull(consumer.getResponseMap().get(MethodsCodes.GET_BANK_BY_ID).poll(15, TimeUnit.SECONDS)).getResponse().get(0);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody BankDTO bankDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_BANK, bankDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.UPDATE_BANK).poll(15, TimeUnit.SECONDS);
        if (errorResponse != null && !errorResponse.getErrors().isEmpty()) {
            throw new ErrorResponseException(errorResponse);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_BANK, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandle(ErrorResponseException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }
}