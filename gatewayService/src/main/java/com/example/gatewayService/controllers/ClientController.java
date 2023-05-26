package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.ClientDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
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
    public String findAll(Model model) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_CLIENTS, new ClientDTO());
        model.addAttribute("clients", consumer.getResponseMap().get(MethodsCodes.GET_ALL_CLIENTS).take().getResponse());
        return "client/getAllClients";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        model.addAttribute("client", consumer.getResponseMap().get(MethodsCodes.GET_CLIENT_BY_ID).take().getResponse().get(0));
        return "client/getClientById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("client") ClientDTO clientDTO, @ModelAttribute("bank") BankDTO bankDTO) {
        return "client/createClient";
    }

    @PostMapping
    public String create(@ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult clientBindingResult,
                         @ModelAttribute("bank") @Valid BankDTO bankDTO,
                         BindingResult bankBindingResult) {
        if (bankBindingResult.hasErrors() || clientBindingResult.hasErrors()) {
            return "client/createClient";
        }
        clientDTO.setBankDTO(bankDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_CLIENT, clientDTO);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/addBank")
    public String createBankToClient(@ModelAttribute("bank") BankDTO bankDTO) {
        return "client/createBankToClient";
    }

    @PostMapping("/{id}")
    public String createBankToClient(@PathVariable("id") int id, @ModelAttribute("bank") @Valid BankDTO bankDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/createBankToClient";
        }
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        clientDTO.setBankDTO(bankDTO);
        producer.sendRequestToClientService(MethodsCodes.CREATE_BANK, clientDTO);
        return "redirect:/clients/" + id;
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        clientDTO = new ClientDTO();
        clientDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_CLIENT_BY_ID, clientDTO);
        clientDTO = (ClientDTO) consumer.getResponseMap().get(MethodsCodes.GET_CLIENT_BY_ID).take().getResponse().get(0);
        model.addAttribute("client", clientDTO);
        model.addAttribute("bank", clientDTO.getBankDTO());
        return "client/updateClient";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult clientBindingResult,
                         @ModelAttribute("bank") BankDTO bankDTO,
                         BindingResult bankBindingResult) {
//        clientDTOUniqueValidator.validate(clientDTO, bindingResult);
        if (clientBindingResult.hasErrors() || bankBindingResult.hasErrors()) {
            return "client/updateClient";
        }
        producer.sendRequestToClientService(MethodsCodes.UPDATE_CLIENT, clientDTO);
        return "redirect:/clients/" + id;
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