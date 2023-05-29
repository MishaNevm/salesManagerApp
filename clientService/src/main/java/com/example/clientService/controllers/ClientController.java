package com.example.clientService.controllers;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.kafka.Producer;
import com.example.clientService.models.Client;
import com.example.clientService.services.BankService;
import com.example.clientService.services.ClientService;
import com.example.clientService.util.*;
import com.example.clientService.util.clientUtil.ClientDTOUniqueValidator;
import com.example.clientService.util.clientUtil.ClientNotFoundException;
import com.example.clientService.util.clientUtil.ClientNotSaveException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ModelMapperUtil modelMapper;
    private final ClientDTOUniqueValidator clientDTOUniqueValidator;
    private final Producer producer;


    @Autowired
    public ClientController(ClientService clientService, ModelMapperUtil modelMapper,
                            ClientDTOUniqueValidator clientDTOUniqueValidator, Producer producer) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.clientDTOUniqueValidator = clientDTOUniqueValidator;
        this.producer = producer;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> findAll() {
         producer.sendMessageToClientTopicResponse(MethodsCodes.GET_ALL_CLIENTS.getCode(), clientService.findAll());
         return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessageToClientTopicResponse(MethodsCodes.GET_CLIENT_BY_ID.getCode(), clientService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public int create(@RequestBody @Valid ClientDTO clientDTO) {
//        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
//                if (bindingResult.hasErrors()) {
//            throw new ClientNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        clientService.save(client);
        return client.getId();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ClientDTO clientDTO) {
        clientDTO.setCreatedAt(clientService.findById(clientDTO.getId()).getResponse().get(0).getCreatedAt());
//        clientDTO.setId(id);
//        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new ClientNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        clientService.update(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        clientService.findById(id);
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ClientNotSaveException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(ClientNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Клиент не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(InvalidFormatException e) {
        return new ResponseEntity<>
                (new ErrorResponse("Тип юридического лица должен быть один из: IP, OOO, AO, ZAO, NKO, PAO, KFH")
                        , HttpStatus.BAD_REQUEST);
    }
}
