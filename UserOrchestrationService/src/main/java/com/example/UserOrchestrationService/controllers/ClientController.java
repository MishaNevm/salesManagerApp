package com.example.UserOrchestrationService.controllers;

import com.example.UserOrchestrationService.dto.BankDTO;
import com.example.UserOrchestrationService.dto.ClientDTO;
import com.example.UserOrchestrationService.dto.ClientDTOResponse;
import com.example.UserOrchestrationService.kafka.Consumer;
import com.example.UserOrchestrationService.kafka.Producer;
import com.example.UserOrchestrationService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final Consumer consumer;
    private final Producer producer;
    private final GlobalExceptionHandler globalExceptionHandler;

    public ClientController(Consumer consumer, Producer producer, GlobalExceptionHandler globalExceptionHandler) {
        this.consumer = consumer;
        this.producer = producer;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping
    public ClientDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_CLIENTS, new ClientDTO());
        return waitForResponse(MethodsCodes.GET_ALL_CLIENTS);
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable("id") int id) throws InterruptedException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        return waitForResponse(MethodsCodes.GET_CLIENT_BY_ID).getResponse().get(0);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody ClientDTO clientDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.CREATE_CLIENT, clientDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.CREATE_CLIENT);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public HttpStatus createBankToClient(@PathVariable("id") int id, @RequestBody BankDTO bankDTO) throws InterruptedException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        bankDTO.setClientDTO(clientDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_BANK, bankDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.CREATE_BANK);
        return HttpStatus.OK;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody ClientDTO clientDTO) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_CLIENT, clientDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.UPDATE_CLIENT);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private ClientDTOResponse waitForResponse(MethodsCodes methodCode) throws InterruptedException {
        return (ClientDTOResponse) consumer.getResponseMap().get(methodCode).poll(15, TimeUnit.SECONDS);
    }
}
