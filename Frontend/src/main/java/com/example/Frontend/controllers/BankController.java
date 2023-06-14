package com.example.Frontend.controllers;


import com.example.Frontend.dto.BankDTO;
import com.example.Frontend.dto.BankDTOResponse;
import com.example.Frontend.util.CurrentUser;
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

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/banks")
public class BankController {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final CurrentUser currentUser;
    private final String GET_ALL_BANKS = "http://localhost:8484/banks";
    private final String GET_BANK_BY_ID = "http://localhost:8484/banks/%d";
    private final String UPDATE_BANK = GET_BANK_BY_ID;
    private final String DELETE_BANK = GET_BANK_BY_ID;

    @Autowired
    public BankController(RestTemplate restTemplate, ObjectMapper objectMapper, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currentUser = currentUser;
    }


    @GetMapping()
    public String findAll(Model model) {
        model.addAttribute("banks", Objects.requireNonNull(restTemplate.getForObject(GET_ALL_BANKS, BankDTOResponse.class)).getResponse());
        return "bank/getAllBanks";
    }

    @GetMapping("/{id}")
    private String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("bank", restTemplate.getForObject(String.format(GET_BANK_BY_ID, id), BankDTO.class));
        return "bank/getBankById";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        BankDTO bankDTO = restTemplate.getForObject(String.format(GET_BANK_BY_ID, id), BankDTO.class);
        model.addAttribute("bank", bankDTO);
        return "bank/updateBank";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("bank") BankDTO bankDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bank/updateBank";
        }
        try {
            restTemplate.patchForObject(String.format(UPDATE_BANK, id), bankDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                bankDTO.setUpdatedBy(currentUser.getEmail());
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "bank/updateBank";
        }
        if (bankDTO.getClientDTO() != null) {
            return "redirect:/clients/" + bankDTO.getClientDTO().getId();
        } else return "redirect:/banks/" + id;
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         @RequestParam(value = "client-id", required = false) Integer clientId) {
        restTemplate.delete(String.format(DELETE_BANK, id));
        if (clientId == null) {
            return "redirect:/banks";
        } else return "redirect:/clients/" + clientId;
    }
}
