package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.ClientDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final Consumer consumer;
    private final Producer producer;

    private ClientDTO clientDTO;

    public ClientController(Consumer consumer, Producer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }


    @GetMapping
    public ResponseEntity<ClientDTOResponse> findAll() throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_CLIENTS, new ClientDTO());
        return ResponseEntity.ok((ClientDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_CLIENTS).take());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable("id") int id) throws InterruptedException {
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        return ResponseEntity.ok((ClientDTO) consumer.getResponseMap().get(MethodsCodes.GET_CLIENT_BY_ID).take().getResponse().get(0));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ClientDTO clientDTO) {
        producer.sendRequestToClientService(MethodsCodes.CREATE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> createBankToClient(@PathVariable("id") int id, @RequestBody @Valid BankDTO bankDTO) {
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        clientDTO.setBankDTO(bankDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_BANK, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody @Valid ClientDTO clientDTO) {
        clientDTO.setId(id);
//        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new ClientNotSaveException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
        producer.sendRequestToClientService(MethodsCodes.UPDATE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ClientNotSaveException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ClientNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Клиент не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(InvalidFormatException e) {
//        return new ResponseEntity<>
//                (new ErrorResponse("Тип юридического лица должен быть один из: IP, OOO, AO, ZAO, NKO, PAO, KFH")
//                        , HttpStatus.BAD_REQUEST);
//    }
}