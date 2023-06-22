package com.example.Frontend.controllers;


import com.example.Frontend.dto.BankDTO;
import com.example.Frontend.dto.BankDTOResponse;
import com.example.Frontend.util.CurrentUser;
import com.example.Frontend.util.ErrorResponse;
import com.example.Frontend.util.Urls;
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

    @Autowired
    public BankController(RestTemplate restTemplate, ObjectMapper objectMapper, CurrentUser currentUser) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.currentUser = currentUser;
    }



    @GetMapping()
    public String findAll(Model model) {
        BankDTOResponse bankDTOResponse = restTemplate.getForObject(Urls.GET_ALL_BANKS.getUrl(), BankDTOResponse.class);
        model.addAttribute("banks", Objects.requireNonNull(bankDTOResponse).getResponse());
        return "bank/getAllBanks";
    }


    @GetMapping("/{id}")
    private String findById(@PathVariable("id") int id, Model model) {
        BankDTO bankDTO = restTemplate.getForObject(String.format(Urls.GET_BANK_BY_ID.getUrl(), id), BankDTO.class);
        model.addAttribute("bank", bankDTO);
        return "bank/getBankById";
    }



    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        BankDTO bankDTO = restTemplate.getForObject(String.format(Urls.GET_BANK_BY_ID.getUrl(), id), BankDTO.class);
        model.addAttribute("bank", bankDTO);
        return "bank/updateBank";
    }



    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("bank") BankDTO bankDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bank/updateBank";
        }
        try {
            bankDTO.setUpdatedBy(currentUser.getEmail());
            restTemplate.patchForObject(String.format(Urls.UPDATE_BANK.getUrl(), id), bankDTO, HttpStatus.class);
        } catch (HttpClientErrorException.BadRequest e) {
            try {
                ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
                errorResponse.getErrors().forEach(a -> bindingResult.rejectValue(a.getField(), a.getCode(), a.getMessage()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "bank/updateBank";
        }
        if (bankDTO.getClientDTO() != null) {
            return "redirect:/clients/" + bankDTO.getClientDTO().getId();
        } else {
            return "redirect:/banks/" + id;
        }
    }



    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, @RequestParam(value = "client-id", required = false) Integer clientId) {
        restTemplate.delete(String.format(Urls.DELETE_BANK.getUrl(), id));
        if (clientId == null) {
            return "redirect:/banks";
        } else {
            return "redirect:/clients/" + clientId;
        }
    }
}

