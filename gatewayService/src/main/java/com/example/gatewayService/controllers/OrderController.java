package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.OrderDTO;
import com.example.gatewayService.dto.OrderDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final Producer producer;
    private final Consumer consumer;

    public OrderController(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @GetMapping
    public ResponseEntity<OrderDTOResponse> findAll() throws InterruptedException {
        producer.sendRequestToOrderService(MethodsCodes.GET_ALL_ORDERS, new OrderDTO());
        return ResponseEntity.ok((OrderDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_ORDERS).take());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable("id") int id) throws InterruptedException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.GET_ORDER_BY_ID, orderDTO);
        return ResponseEntity.ok((OrderDTO) consumer.getResponseMap().get(MethodsCodes.GET_ORDER_BY_ID).take().getResponse().get(0));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid OrderDTO orderDTO) {
        producer.sendRequestToOrderService(MethodsCodes.CREATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id ,@RequestBody @Valid OrderDTO orderDTO) {
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.UPDATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.DELETE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @PostMapping("/{id}/add-product")
//    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
//                                                        @RequestParam("product-id") Integer productId,
//                                                        @RequestParam("quantity") Integer quantity) {
//        orderService.addProductToOrder(orderService.findById(id), productId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
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


//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

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
