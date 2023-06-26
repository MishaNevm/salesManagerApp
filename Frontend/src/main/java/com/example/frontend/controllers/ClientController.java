package com.example.frontend.controllers;


import com.example.frontend.dto.BankDTO;
import com.example.frontend.dto.ClientDTO;
import com.example.frontend.dto.ClientDTOResponse;
import com.example.frontend.dto.OrderDTOResponse;
import com.example.frontend.util.ClientTypes;
import com.example.frontend.util.CurrentUser;
import com.example.frontend.util.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final RestTemplate restTemplate;
    private final CurrentUser currentUser;

    @Autowired
    public ClientController(RestTemplate restTemplate, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.currentUser = currentUser;
    }

    @GetMapping
    public String getAll(Model model) {
        ClientDTOResponse response = restTemplate.getForObject(Urls.GET_ALL_CLIENTS.getUrl(), ClientDTOResponse.class);
        model.addAttribute("clients", response != null ? response.getResponse() : Collections.emptyList());
        return "client/getAllClients";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute("role", currentUser.getRole());
        ClientDTO clientDTO = restTemplate.getForObject(String.format(Urls.GET_CLIENT_BY_ID.getUrl(), id), ClientDTO.class);
        if (clientDTO != null) {
            model.addAttribute("client", clientDTO);
            model.addAttribute("orders", Objects.requireNonNull(restTemplate
                            .getForObject
                                    (String.format(Urls.GET_ORDERS_BY_CLIENT_SHORT_NAME.getUrl(),
                                                    clientDTO.getShortName()),
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
            GlobalExceptionHandler.handleBadRequestException(e, bindingResult);
            return "client/createClient";
        }
        return "redirect:/clients";
    }

    @GetMapping("/{id}/add-bank")
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
            GlobalExceptionHandler.handleBadRequestException(e, bindingResult);
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
            GlobalExceptionHandler.handleBadRequestException(e, bindingResult);
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