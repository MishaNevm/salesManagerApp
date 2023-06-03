package com.example.gatewayService.controllers;

import com.example.gatewayService.dto.ProductDTO;
import com.example.gatewayService.dto.ProductDTOResponse;
import com.example.gatewayService.dto.ProductOrderDTO;
import com.example.gatewayService.dto.ProductOrderDTOResponse;
import com.example.gatewayService.kafka.Consumer;
import com.example.gatewayService.kafka.Producer;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return (ProductDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_ALL_PRODUCTS).take();
    }

    @GetMapping("/get-products-by-order")
    public ProductDTOResponse findAllProductsByOrderId(@RequestParam(value = "order-id", required = false) Integer orderId) throws InterruptedException {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID, productOrderDTO);
        return (ProductDTOResponse) consumer.getResponseMap().get(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID).take();
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable("id") int id) throws InterruptedException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        producer.sendRequestToInventoryService(MethodsCodes.GET_PRODUCT_BY_ID, productDTO);
        return (ProductDTO) consumer.getResponseMap().get(MethodsCodes.GET_PRODUCT_BY_ID).take().getResponse().get(0);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ProductDTO productDTO) {
        producer.sendRequestToInventoryService(MethodsCodes.CREATE_PRODUCT, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ProductDTO productDTO) {
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

//    @PostMapping("/{id}/add-to-order")
//    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
//                                                        @RequestParam(value = "order-id", required = false) Integer orderId,
//                                                        @RequestParam(value = "quantity", required = false) Integer quantity) {
//        productService.addProductToOrder(productService.findById(id), orderId, quantity);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

//    @PatchMapping("/{id}/change-order")
//    public ResponseEntity<HttpStatus> changeOrderInProduct(@PathVariable("id") int id,
//                                                           @RequestParam(value = "old-order-id", required = false) Integer oldOrderId,
//                                                           @RequestParam(value = "new-order-id", required = false) Integer newOrderId) {
//        productService.findById(id);
////        productService.changeOrderInProduct(id, oldOrderId, newOrderId);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotSaveException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotFoundException e) {
//        return new ResponseEntity<>(new ErrorResponse("Товар не найден"), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exceptionHandler(ProductNotAddException e) {
//        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
}
