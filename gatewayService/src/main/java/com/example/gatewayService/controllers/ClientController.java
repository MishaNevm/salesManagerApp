package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.ClientDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return (ClientDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_CLIENTS).take();
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        return (ClientDTO) consumer.getResponseMap().get(MethodsCodes.GET_CLIENT_BY_ID).take().getResponse().get(0);
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ClientDTO clientDTO) {
        producer.sendRequestToClientService(MethodsCodes.CREATE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> createBankToClient(@PathVariable("id") int id, @RequestBody @Valid BankDTO bankDTO) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        bankDTO.setClientDTO(clientDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_BANK, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ClientDTO clientDTO) {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_CLIENT, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@RequestBody ClientDTO clientDTO) {
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