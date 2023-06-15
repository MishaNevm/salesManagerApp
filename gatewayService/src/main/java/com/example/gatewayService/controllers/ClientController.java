package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.ClientDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final Consumer consumer;
    private final Producer producer;

    public ClientController(Consumer consumer, Producer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }


    @GetMapping
    public ClientDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_CLIENTS, new ClientDTO());
        return (ClientDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_CLIENTS).poll(15, TimeUnit.SECONDS);
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable("id") int id) throws InterruptedException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        return (ClientDTO) Objects.requireNonNull(consumer.getResponseMap().get(MethodsCodes.GET_CLIENT_BY_ID).poll(15, TimeUnit.SECONDS)).getResponse().get(0);
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ClientDTO clientDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.CREATE_CLIENT, clientDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.CREATE_CLIENT).poll(15, TimeUnit.SECONDS);
        ErrorResponseException.checkErrorResponse(errorResponse);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public HttpStatus createBankToClient(@PathVariable("id") int id, @RequestBody @Valid BankDTO bankDTO) throws InterruptedException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        bankDTO.setClientDTO(clientDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_BANK, bankDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.CREATE_BANK).poll(15, TimeUnit.SECONDS);
        ErrorResponseException.checkErrorResponse(errorResponse);
        return HttpStatus.OK;
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ClientDTO clientDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_CLIENT, clientDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.UPDATE_CLIENT).poll(15, TimeUnit.SECONDS);
        ErrorResponseException.checkErrorResponse(errorResponse);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}