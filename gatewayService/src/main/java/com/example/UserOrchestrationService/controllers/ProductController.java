package com.example.UserOrchestrationService.controllers;

import com.example.UserOrchestrationService.dto.ProductDTO;
import com.example.UserOrchestrationService.dto.ProductDTOResponse;
import com.example.UserOrchestrationService.dto.ProductOrderDTO;
import com.example.UserOrchestrationService.kafka.Consumer;
import com.example.UserOrchestrationService.kafka.Producer;
import com.example.UserOrchestrationService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Consumer consumer;
    private final Producer producer;

    @Autowired

    public ProductController(Consumer consumer, Producer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }


    @GetMapping
    public ProductDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToInventoryService(MethodsCodes.GET_ALL_PRODUCTS, new ProductDTO());
        return (ProductDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_PRODUCTS).poll(15, TimeUnit.SECONDS);
    }

    @GetMapping("/get-products-by-order")
    public ProductDTOResponse findAllProductsByOrderId(@RequestParam(value = "order-id", required = false) Integer orderId) throws InterruptedException {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID, productOrderDTO);
        return (ProductDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID).poll(15, TimeUnit.SECONDS);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") int id) throws InterruptedException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCT_BY_ID, productDTO);
        return (ProductDTO) Objects.requireNonNull(consumer.getResponseMap().get(MethodsCodes.GET_PRODUCT_BY_ID).poll(15, TimeUnit.SECONDS)).getResponse().get(0);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody ProductDTO productDTO) {
        producer.sendRequestToInventoryService(MethodsCodes.CREATE_PRODUCT, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody ProductDTO productDTO) {
        producer.sendRequestToInventoryService(MethodsCodes.UPDATE_PRODUCT, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_PRODUCT, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
