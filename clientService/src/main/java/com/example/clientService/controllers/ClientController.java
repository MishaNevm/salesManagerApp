package com.example.clientService.controllers;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.models.Client;
import com.example.clientService.services.ClientService;
import com.example.clientService.util.*;
import com.example.clientService.util.clientUtil.ClientDTOUniqueValidator;
import com.example.clientService.util.clientUtil.ClientNotFoundException;
import com.example.clientService.util.clientUtil.ClientNotSaveException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ModelMapperUtil modelMapper;
    private final ClientDTOUniqueValidator clientDTOUniqueValidator;


    @Autowired
    public ClientController(ClientService clientService, ModelMapperUtil modelMapper,
                            ClientDTOUniqueValidator clientDTOUniqueValidator) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.clientDTOUniqueValidator = clientDTOUniqueValidator;
    }

    @GetMapping
    public List<ClientDTO> findAll() {
        return clientService.findAll().stream().map(modelMapper::convertClientToClientDTO).toList();
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable("id") int id) {
        return modelMapper.convertClientToClientDTO(clientService.findById(id));
    }

    @PostMapping
    public int create(@RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult) {
        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
                if (bindingResult.hasErrors()) {
            throw new ClientNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        clientService.save(client);
        return client.getId();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult) {
        clientDTO.setCreatedAt(clientService.findById(id).getCreatedAt());
        clientDTO.setId(id);
        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ClientNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
        }
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
