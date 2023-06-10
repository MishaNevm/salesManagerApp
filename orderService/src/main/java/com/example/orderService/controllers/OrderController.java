package com.example.orderService.controllers;

import com.example.orderService.dto.OrderDTO;
import com.example.orderService.kafka.Producer;
import com.example.orderService.services.OrderService;
import com.example.orderService.util.ErrorResponse;
import com.example.orderService.util.MethodsCodes;
import com.example.orderService.util.OrderNotCreatedException;
import com.example.orderService.util.OrderNotFoundException;
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
    public ResponseEntity<HttpStatus> findByClientId(@RequestParam(value = "client-id", required = false) int clientId) {
        producer.sendMessageToOrderResponseTopic(MethodsCodes.GET_ORDERS_BY_CLIENT_ID.getCode(), orderService.findByClientId(clientId));
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
        orderDTO.setCreatedAt(orderService.findById(orderDTO.getId()).getResponse().get(0).getCreatedAt());
        orderService.update(orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        orderService.findById(id);
        orderService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


//    @PatchMapping("/{id}/set-product-quantity")
//    public ResponseEntity<HttpStatus> setProductQuantity(@PathVariable("id") int id,
//                                                         @RequestParam(value = "product-id", required = false) Integer productId,
//                                                         @RequestParam(value = "quantity", required = false) Integer quantity) {
//        orderService.setQuantityProductInOrder(id, productId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}/deleteProduct")
//    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id,
//                                                    @RequestParam(value = "product-id", required = false) Integer productId) {
//        orderService.findById(id);
//        if (productId == null) {
//            orderService.deleteAllProductsFromOrder(id);
//        } else {
//            orderService.deleteProductFromOrder(id, productId);
//        }
//        return ResponseEntity.ok(HttpStatus.OK);
//    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

//    private Order checkRequest(Integer clientId, OrderDTO orderDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            throw new OrderNotCreatedException(ErrorResponse.convertErrorsToMessage(bindingResult));
//        }
//        Order order = modelMapperUtil.convertOrderDTOToOrder(orderDTO);
//        if (clientId != null) {
//            order.setClientId(clientId);
//        }
//        return order;
//    }
}
