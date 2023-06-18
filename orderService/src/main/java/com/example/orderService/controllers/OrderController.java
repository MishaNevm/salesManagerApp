package com.example.orderService.controllers;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.kafka.Producer;
import com.example.orderService.services.OrderService;
import com.example.orderService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final Producer producer;

    public OrderController(OrderService orderService, Producer producer) {
        this.orderService = orderService;
        this.producer = producer;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> findAll() {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ALL_ORDERS.getCode(), orderService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<HttpStatus> findByClientShortName(@RequestParam(value = "client-short-name", required = false) String clientShortName) {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME.getCode(), orderService.findByClientShortName(clientShortName));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDER_BY_ID.getCode(), orderService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid OrderDTO orderDTO) {
        orderService.save(orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid OrderDTO orderDTO) {
        orderService.update(orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        orderService.findById(id);
        orderService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
