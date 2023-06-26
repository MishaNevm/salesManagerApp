package com.example.userOrchestrationService.controllers;

import com.example.userOrchestrationService.dto.ProductDTO;
import com.example.userOrchestrationService.dto.ProductDTOResponse;
import com.example.userOrchestrationService.dto.ProductOrderDTO;
import com.example.userOrchestrationService.kafka.Consumer;
import com.example.userOrchestrationService.kafka.Producer;
import com.example.userOrchestrationService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final Consumer consumer;
    private final Producer producer;

    public ProductController(Consumer consumer, Producer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }

    @GetMapping
    public ProductDTOResponse findAll() throws InterruptedException {
        producer.sendRequestToInventoryService(MethodsCodes.GET_ALL_PRODUCTS, new ProductDTO());
        return waitForResponse(MethodsCodes.GET_ALL_PRODUCTS);
    }

    @GetMapping("/get-products-by-order")
    public ProductDTOResponse findAllProductsByOrderId(@RequestParam(value = "order-id", required = false) Integer orderId) throws InterruptedException {
        ProductOrderDTO productOrderDTO = createProductOrderDTO(orderId);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID, productOrderDTO);
        return waitForResponse(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") int id) throws InterruptedException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCT_BY_ID, productDTO);
        return waitForResponse(MethodsCodes.GET_PRODUCT_BY_ID).getResponse().get(0);
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

    private ProductDTOResponse waitForResponse(MethodsCodes methodCode) throws InterruptedException {
        return (ProductDTOResponse) consumer.getResponseMap().get(methodCode).poll(15, TimeUnit.SECONDS);
    }

    private ProductOrderDTO createProductOrderDTO(Integer orderId) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        return productOrderDTO;
    }
}

