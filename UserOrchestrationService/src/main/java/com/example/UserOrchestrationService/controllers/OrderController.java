package com.example.UserOrchestrationService.controllers;


import com.example.UserOrchestrationService.dto.OrderDTO;
import com.example.UserOrchestrationService.dto.OrderDTOResponse;
import com.example.UserOrchestrationService.dto.ProductDTO;
import com.example.UserOrchestrationService.dto.ProductOrderDTO;
import com.example.UserOrchestrationService.kafka.Consumer;
import com.example.UserOrchestrationService.kafka.Producer;
import com.example.UserOrchestrationService.util.MethodsCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final Producer producer;
    private final Consumer consumer;
    private final GlobalExceptionHandler globalExceptionHandler;

    public OrderController(Producer producer, Consumer consumer, GlobalExceptionHandler globalExceptionHandler) {
        this.producer = producer;
        this.consumer = consumer;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @GetMapping
    public OrderDTOResponse findAll(@RequestParam(value = "client-short-name", required = false) String clientShortName) throws InterruptedException {
        if (clientShortName != null) {
            producer.sendRequestToOrderService(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME, clientShortName);
            return waitForResponse(MethodsCodes.GET_ORDERS_BY_CLIENT_SHORT_NAME);
        } else {
            producer.sendRequestToOrderService(MethodsCodes.GET_ALL_ORDERS, new OrderDTO());
            return waitForResponse(MethodsCodes.GET_ALL_ORDERS);
        }
    }

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") int id) throws InterruptedException {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.GET_ORDER_BY_ID, orderDTO);
        return waitForResponse(MethodsCodes.GET_ORDER_BY_ID).getResponse().get(0);
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
    public ResponseEntity<HttpStatus> updateProductQuantityInOrder(@PathVariable("id") int orderId,
                                                                   @RequestParam(value = "product-id", required = false) Integer productId,
                                                                   @RequestParam(value = "quantity", required = false) Integer quantity) throws InterruptedException {
        ProductOrderDTO productOrderDTO = createProductOrderDTO(orderId, productId, quantity);
        producer.sendRequestToInventoryService(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER, productOrderDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete-from-order")
    public ResponseEntity<HttpStatus> deleteByOrderIdAndProductId(@PathVariable("id") int orderId,
                                                                  @RequestParam(value = "product-id", required = false) Integer productId) {
        ProductOrderDTO productOrderDTO = createProductOrderDTO(orderId, productId, null);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_PRODUCT_BY_ORDER_ID_AND_PRODUCT_ID, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete-products")
    public ResponseEntity<HttpStatus> deleteProductsInOrderByOrderId(@PathVariable("id") int id) {
        ProductOrderDTO productOrderDTO = createProductOrderDTO(id, null, null);
        producer.sendRequestToInventoryService(MethodsCodes.DELETE_ALL_PRODUCTS_IN_ORDER_BY_ORDER_ID, productOrderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        producer.sendRequestToOrderService(MethodsCodes.DELETE_ORDER, orderDTO);
        return deleteProductsInOrderByOrderId(id);
    }

    @PostMapping("/{id}/add-product")
    public ResponseEntity<HttpStatus> addProductToOrder(@PathVariable("id") int id,
                                                        @RequestParam("product-id") Integer productId,
                                                        @RequestParam("quantity") Integer quantity) throws InterruptedException {
        ProductOrderDTO productOrderDTO = createProductOrderDTO(id, productId, quantity);
        producer.sendRequestToInventoryService(MethodsCodes.ADD_PRODUCT_TO_ORDER, productOrderDTO);
        globalExceptionHandler.checkErrorResponse(MethodsCodes.ADD_PRODUCT_TO_ORDER);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private OrderDTOResponse waitForResponse(MethodsCodes methodCode) throws InterruptedException {
        return (OrderDTOResponse) consumer.getResponseMap().get(methodCode).poll(15, TimeUnit.SECONDS);
    }

    private ProductOrderDTO createProductOrderDTO(int orderId, Integer productId, Integer quantity) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        productOrderDTO.setOrderId(orderId);
        if (productId != null) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productId);
            productOrderDTO.setProduct(productDTO);
        }
        productOrderDTO.setQuantity(quantity);
        return productOrderDTO;
    }
}

