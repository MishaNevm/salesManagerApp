package com.example.inventoryService.controllers;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.kafka.Producer;
import com.example.inventoryService.services.ProductOrderService;
import com.example.inventoryService.services.ProductService;
import com.example.inventoryService.util.ErrorResponse;
import com.example.inventoryService.util.MethodsCodes;
import com.example.inventoryService.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Producer producer;
    private final ProductOrderService productOrderService;

    @Autowired
    public ProductController(ProductService productService, Producer producer, ProductOrderService productOrderService) {
        this.productService = productService;
        this.producer = producer;
        this.productOrderService = productOrderService;
    }

    @GetMapping
    public ResponseEntity<HttpStatus> findAll() {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_ALL_PRODUCTS.getCode(), productService.findAll());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get-products-by-order")
    public ResponseEntity<HttpStatus> findAllProductsByOrderId(@RequestParam(value = "order-id", required = false) Integer orderId) {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID.getCode(), productOrderService.findByOrderId(orderId));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> findById(@PathVariable("id") int id) {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_PRODUCT_BY_ID.getCode(), productService.findById(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid ProductDTO productDTO) {
        productService.save(productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/add-to-order")
    public ErrorResponse addProductToOrder(@RequestBody ProductOrderDTO productOrderDTO) {
        ValidationError validationError = new ValidationError();
        productOrderService.save(productOrderDTO, validationError);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(Collections.singletonList(validationError));
        return errorResponse;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ProductDTO productDTO) {
        productService.update(productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/update-quantity-in-order")
    public ErrorResponse updateProductQuantityInOrder(ProductOrderDTO productOrderDTO) {
        ValidationError validationError = new ValidationError();
        productOrderService.updateProductQuantityInOrder(productOrderDTO, validationError);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(Collections.singletonList(validationError));
        return errorResponse;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        productService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete-from-order")
    public ResponseEntity<HttpStatus> deleteByOrderIdAndProductId(@RequestParam(value = "orderId", required = false) Integer orderId, @PathVariable("id") int productId) {
        productOrderService.deleteByOrderIdAndProductId(orderId, productId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteAllProductsInOrderByOrderId(@RequestParam(value = "orderId", required = false) Integer orderId) {
        productOrderService.deleteAllProductsByOrderId(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
