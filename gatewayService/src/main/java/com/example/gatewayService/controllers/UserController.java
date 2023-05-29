package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final Producer producer;

    private final Consumer consumer;

    public UserController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @GetMapping()
    public String getAllUsers(Model model) throws InterruptedException {
        producer.sendRequestToUserService(MethodsCodes.GET_ALL_USERS, null);
        model.addAttribute("users", consumer.getResponseMap().get(MethodsCodes.GET_ALL_USERS).take().getResponse());
        return "user/getAllUsers";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) throws InterruptedException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_ID, userDTO);
        model.addAttribute("user", consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_ID)
                .take().getResponse().get(0));
        return "user/getUserById";
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam(value = "email", required = false) String email) throws InterruptedException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_EMAIL, userDTO);
        return ResponseEntity
                .ok(
                        ((UserDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_EMAIL)
                                .take())
                                .getResponse()
                                .get(0));
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("user") UserDTO userDTO) {
        return "user/createUser";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "user/createUser";
        producer.sendRequestToUserService(MethodsCodes.CREATE_USER, user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) throws InterruptedException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.GET_USER_BY_ID, userDTO);
        model.addAttribute("user", consumer.getResponseMap().get(MethodsCodes.GET_USER_BY_ID)
                .take().getResponse().get(0));
        return "user/updateUser";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid UserDTO user, BindingResult bindingResult) {
        producer.sendRequestToUserService(MethodsCodes.UPDATE_USER, user);
        if (bindingResult.hasErrors()) return "user/updateUser";
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        producer.sendRequestToUserService(MethodsCodes.DELETE_USER, userDTO);
        return "redirect:/users";
    }
}

