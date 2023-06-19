package com.example.Frontend.controllers;


import com.example.Frontend.dto.BankDTO;
import com.example.Frontend.dto.ClientDTO;
import com.example.Frontend.dto.ClientDTOResponse;
import com.example.Frontend.dto.OrderDTOResponse;
import com.example.Frontend.util.ClientTypes;
import com.example.Frontend.util.CurrentUser;
import com.example.Frontend.util.ErrorResponse;
import com.example.Frontend.util.Urls;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CurrentUser currentUser;


    @Autowired
    public ClientController(RestTemplate restTemplate, ObjectMapper objectMapper, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("clients", Objects.requireNonNull(restTemplate.getForObject(Urls.GET_ALL_CLIENTS.getUrl(), ClientDTOResponse.class)).getResponse());
        return "client/getAllClients";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("role", currentUser.getRole());
        ClientDTO clientDTO = restTemplate.getForObject(String.format(Urls.GET_CLIENT_BY_ID.getUrl(), id), ClientDTO.class);
        if (clientDTO != null) {
            model.addAttribute("client", clientDTO);
            model.addAttribute("orders", Objects.requireNonNull(restTemplate
                            .getForObject(String
                                            .format(Urls.GET_ORDERS_BY_CLIENT_SHORT_NAME.getUrl(), clientDTO.getShortName()),
                                    OrderDTOResponse.class))
                    .getResponse());
        }
        return "client/getClientById";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("types", ClientTypes.values());
        return "client/createClient";
    }

    @PostMapping
    public String create(Model model, @ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult bindingResult) {
        model.addAttribute("types", ClientTypes.values());
        if (bindingResult.hasErrors()) {
            return "client/createClient";
        }
        try {
            clientDTO.setCreatedBy(currentUser.getEmail());
            restTemplate.postForObject(Urls.CREATE_CLIENT.getUrl(), clientDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "client/createClient";
        }
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
            bankDTO.setCreatedBy(currentUser.getEmail());
            restTemplate.postForObject(String.format(Urls.CREATE_BANK_TO_CLIENT.getUrl(), id), bankDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
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
        model.addAttribute("client", restTemplate.getForObject(String.format(Urls.GET_CLIENT_BY_ID.getUrl(), id), ClientDTO.class));
        model.addAttribute("types", ClientTypes.values());
        return "client/updateClient";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, Model model,
                         @ModelAttribute("client") @Valid ClientDTO clientDTO,
                         BindingResult bindingResult) {
        model.addAttribute("types", ClientTypes.values());
        if (bindingResult.hasErrors()) {
            return "client/updateClient";
        }
        try {
            clientDTO.setUpdatedBy(currentUser.getEmail());
            restTemplate.patchForObject(String.format(Urls.UPDATE_CLIENT.getUrl(), id), clientDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "client/updateClient";
        }
        return "redirect:/clients/" + id;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        restTemplate.delete(String.format(Urls.DELETE_CLIENT.getUrl(), id));
        return "redirect:/clients";
    }
}