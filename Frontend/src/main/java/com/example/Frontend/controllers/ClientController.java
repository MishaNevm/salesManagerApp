package com.example.Frontend.controllers;


import com.example.Frontend.dto.BankDTO;
import com.example.Frontend.dto.ClientDTO;
import com.example.Frontend.dto.ClientDTOResponse;
import com.example.Frontend.dto.OrderDTOResponse;
import com.example.Frontend.util.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    protected final static String GET_ALL_CLIENTS = "http://localhost:8484/clients";
    protected final static String GET_CLIENT_BY_ID = "http://localhost:8484/clients/";
    protected final static String CREATE_CLIENT = GET_ALL_CLIENTS;
    protected final static String CREATE_BANK_TO_CLIENT = GET_CLIENT_BY_ID;
    protected final static String UPDATE_CLIENT = GET_CLIENT_BY_ID;
    protected final static String DELETE_CLIENT = GET_CLIENT_BY_ID;

    @Autowired
    public ClientController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("clients", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_CLIENTS, ClientDTOResponse.class)).getResponse());
        return "client/getAllClients";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("client", restTemplate.getForObject(GET_CLIENT_BY_ID + id, ClientDTO.class));
        model.addAttribute("orders", Objects.requireNonNull(restTemplate
                        .getForObject(String
                                        .format(OrderController
                                                .GET_ORDERS_BY_CLIENT_ID, id),
                                OrderDTOResponse.class))
                .getResponse());
        return "client/getClientById";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("client") ClientDTO clientDTO) {
        return "client/createClient";
    }

    @PostMapping
    public String create(@ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/createClient";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientDTO> entity = new HttpEntity<>(clientDTO, headers);
        restTemplate.exchange(CREATE_CLIENT, HttpMethod.POST, entity, HttpStatus.class);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/addBank")
    public String createBankToClient(@PathVariable("id") int id, Model model, @ModelAttribute("bank") BankDTO bankDTO) {
        model.addAttribute("id", id);
        return "client/createBankToClient";
    }

    @PostMapping("/{id}")
    public String createBankToClient(@PathVariable("id") int id, @ModelAttribute("bank") @Valid BankDTO bankDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/createBankToClient";
        }

        bankDTO.setClientDTO(new ClientDTO(id));

        try {
            restTemplate.postForObject(CREATE_BANK_TO_CLIENT + id, bankDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            String responseBody = e.getResponseBodyAsString();
            try {
                ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "client/createBankToClient";
        }

        return "redirect:/clients/" + id;
    }



    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        ClientDTO clientDTO = restTemplate.getForObject(GET_CLIENT_BY_ID + id, ClientDTO.class);
        model.addAttribute("client", clientDTO);
        return "client/updateClient";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/updateClient";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClientDTO> entity = new HttpEntity<>(clientDTO, headers);
        restTemplate.exchange(UPDATE_CLIENT + id, HttpMethod.PATCH, entity, HttpStatus.class);
        return "redirect:/clients/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, @ModelAttribute("client") ClientDTO clientDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientDTO> entity = new HttpEntity<>(clientDTO, headers);
        restTemplate.exchange(DELETE_CLIENT + id, HttpMethod.DELETE, entity, HttpStatus.class);
        return "redirect:/clients";
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