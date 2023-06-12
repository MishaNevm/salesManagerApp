package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.OrderDTO;
import com.example.gatewayService.dto.OrderDTOResponse;
import com.example.gatewayService.dto.ProductDTO;
import com.example.gatewayService.dto.ProductOrderDTO;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    public OrderDTOResponse findAll(@RequestParam(value = "client-id", required = false) Integer clientId) throws InterruptedException {
        OrderDTOResponse orderDTOResponse;
        if (clientId != null) {
            producer.sendRequestToOrderService(MethodsCodes.GET_ORDERS_BY_CLIENT_ID, clientId);
            orderDTOResponse = (OrderDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ORDERS_BY_CLIENT_ID).take();
        } else {
            producer.sendRequestToOrderService(MethodsCodes.GET_ALL_ORDERS, new OrderDTO());
            orderDTOResponse = (OrderDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_ORDERS).take();
        }
        return orderDTOResponse;
    }

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") int id) throws InterruptedException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.GET_ORDER_BY_ID, orderDTO);
        return (OrderDTO) Objects.requireNonNull(consumer.getResponseMap().get(MethodsCodes.GET_ORDER_BY_ID).poll(15, TimeUnit.SECONDS)).getResponse().get(0);
    }


    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam(value = "client-id", required = false) int clientId) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setClientId(clientId);
        producer.sendRequestToOrderService(MethodsCodes.CREATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int orderId, @RequestParam(value = "client-id", required = false) int clientId) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setClientId(clientId);
        producer.sendRequestToOrderService(MethodsCodes.UPDATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/update-product-quantity")
    public ResponseEntity<HttpStatus> updateProductQuantityInOrder(@PathVariable("id") int orderId, @RequestParam(value = "product-id", required = false) Integer productId,
                                                                   @RequestParam(value = "quantity", required = false) Integer quantity) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productOrderDTO.setProduct(productDTO);
        productOrderDTO.setQuantity(quantity);
        producer.sendRequestToInventoryService(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete-from-order")
    public ResponseEntity<HttpStatus> deleteByOrderIdAndProductId(@PathVariable("id") int orderId, @RequestParam(value = "product-id", required = false) Integer productId) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productOrderDTO.setProduct(productDTO);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_PRODUCT_BY_ORDER_ID_AND_PRODUCT_ID, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete-products")
    public ResponseEntity<HttpStatus> deleteProductsInOrderByOrderId(@PathVariable("id") int id) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(id);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.DELETE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/add-product")
    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
                                                        @RequestParam("product-id") Integer productId,
                                                        @RequestParam("quantity") Integer quantity) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productOrderDTO.setProduct(productDTO);
        productOrderDTO.setOrderId(id);
        productOrderDTO.setQuantity(quantity);
        producer.sendRequestToInventoryService(MethodsCodes.ADD_PRODUCT_TO_ORDER, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Данный заказ не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(OrderNotCreatedException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
}
