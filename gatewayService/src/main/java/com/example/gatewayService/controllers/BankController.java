package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
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
    public String findAll(Model model) throws InterruptedException {
        producer.sendRequestToClientService(MethodsCodes.GET_ALL_BANKS, new BankDTO());
        model.addAttribute("banks", consumer.getResponseMap().get(MethodsCodes.GET_ALL_BANKS).take().getResponse());
        return "bank/getAllBanks";
    }

    @GetMapping("/{id}")
    private String findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_BANK_BY_ID, bankDTO);
        model.addAttribute("bank", consumer.getResponseMap().get(MethodsCodes.GET_BANK_BY_ID).take().getResponse().get(0));
        return "bank/getBankById";
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) throws InterruptedException {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.GET_BANK_BY_ID, bankDTO);
        model.addAttribute("bank", consumer.getResponseMap().get(MethodsCodes.GET_BANK_BY_ID).take().getResponse().get(0));
        return "bank/updateBank";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("bank") BankDTO bankDTO, BindingResult bindingResult) {
//        bankDTOUniqueValidator.validate(bankDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "bank/updateBank";
        }
        producer.sendRequestToClientService(MethodsCodes.UPDATE_BANK, bankDTO);
        if (bankDTO.getClientDTO() != null) {
            return "redirect:/clients/" + bankDTO.getClientDTO().getId();
        } else return "redirect:/banks/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bankDTO = new BankDTO();
        bankDTO.setId(id);
        producer.sendRequestToClientService(MethodsCodes.DELETE_BANK, bankDTO);
        return "redirect:/banks";
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
