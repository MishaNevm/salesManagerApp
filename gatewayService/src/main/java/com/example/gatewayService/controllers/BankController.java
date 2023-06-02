package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.BankDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return (BankDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_BANKS).take();
    }

    @GetMapping("/{id}")
    private BankDTO findById(@PathVariable("id") int id) throws InterruptedException {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_BANK_BY_ID, bankDTO);
        return (BankDTO) consumer.getResponseMap().get(MethodsCodes.GET_BANK_BY_ID).take().getResponse().get(0);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody BankDTO bankDTO) {
        producer.sendRequestToClientService(MethodsCodes.UPDATE_BANK, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_BANK, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandle(BankNotSaveException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandle(BankNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Данный банк не найден"), HttpStatus.BAD_REQUEST);
//    }
}
