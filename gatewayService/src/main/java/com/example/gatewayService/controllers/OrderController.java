package com.example.gatewayService.controllers;


import com.example.gatewayService.dto.OrderDTO;
import com.example.gatewayService.dto.OrderDTOResponse;
import com.example.gatewayService.dto.ProductDTO;
import com.example.gatewayService.dto.ProductOrderDTO;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.ErrorResponse;
import com.example.gatewayService.util.ErrorResponseException;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public OrderDTOResponse findAll(@RequestParam(value = "client-short-name", required = false) String clientShortName) throws InterruptedException {
        OrderDTOResponse orderDTOResponse;
        if (clientShortName != null) {
            producer.sendRequestToOrderService(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME, clientShortName);
            orderDTOResponse = (OrderDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME).poll(15, TimeUnit.SECONDS);
        } else {
            producer.sendRequestToOrderService(MethodsCodes.GET_ALL_ORDERS, new OrderDTO());
            orderDTOResponse = (OrderDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_ORDERS).poll(15, TimeUnit.SECONDS);
        }
        if (orderDTOResponse != null) {
            orderDTOResponse.getResponse().forEach(a -> System.out.println(a.getClientShortName()));
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
    public ResponseEntity<HttpStatus> create(@RequestBody OrderDTO orderDTO) {
        producer.sendRequestToOrderService(MethodsCodes.CREATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody OrderDTO orderDTO) {
        producer.sendRequestToOrderService(MethodsCodes.UPDATE_ORDER, orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/update-product-quantity")
    public ResponseEntity<HttpStatus> updateProductQuantityInOrder(@PathVariable("id") int orderId, @RequestParam(value = "product-id", required = false) Integer productId,
                                                                   @RequestParam(value = "quantity", required = false) Integer quantity) throws InterruptedException {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productOrderDTO.setProduct(productDTO);
        productOrderDTO.setQuantity(quantity);
        producer.sendRequestToInventoryService(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER, productOrderDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER).poll(15, TimeUnit.SECONDS);
        ErrorResponseException.checkErrorResponse(errorResponse);
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
                                                        @RequestParam("quantity") Integer quantity) throws InterruptedException {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productOrderDTO.setProduct(productDTO);
        productOrderDTO.setOrderId(id);
        productOrderDTO.setQuantity(quantity);
        producer.sendRequestToInventoryService(MethodsCodes.ADD_PRODUCT_TO_ORDER, productOrderDTO);
        ErrorResponse errorResponse = consumer.getErrorResponseMap().get(MethodsCodes.ADD_PRODUCT_TO_ORDER).poll(15, TimeUnit.SECONDS);
        ErrorResponseException.checkErrorResponse(errorResponse);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
