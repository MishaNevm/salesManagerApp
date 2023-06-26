package com.example.inventoryService.controllers;

import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.example.inventoryService.kafka.Producer;
import com.example.inventoryService.services.ProductOrderService;
import com.example.inventoryService.services.ProductService;
import com.example.inventoryService.util.ErrorResponse;
import com.example.inventoryService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    public void findAll() {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_ALL_PRODUCTS, productService.findAll());
    }

    public void findAllProductsByOrderId(int orderId) {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_PRODUCTS_BY_ORDER_ID, productOrderService.findByOrderId(orderId));
    }

    public void findById(int id) {
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.GET_PRODUCT_BY_ID, productService.findById(id));
    }

    public void save(ProductDTO productDTO) {
        productService.save(productDTO);
    }

    public void addProductToOrder(ProductOrderDTO productOrderDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        productOrderService.save(productOrderDTO, errorResponse);
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.ADD_PRODUCT_TO_ORDER, errorResponse);
    }

    public void update( ProductDTO productDTO) {
        productService.update(productDTO);
    }

    public void updateProductQuantityInOrder(ProductOrderDTO productOrderDTO) {
        ErrorResponse errorResponse = new ErrorResponse();
        productOrderService.updateProductQuantityInOrder(productOrderDTO, errorResponse);
        producer.sendMessageToInventoryResponseTopic(MethodsCodes.UPDATE_PRODUCT_QUANTITY_IN_ORDER, errorResponse);
    }


    public void delete(int id) {
        productService.delete(id);
    }

    public void deleteByOrderIdAndProductId(int orderId, int productId) {
        productOrderService.deleteByOrderIdAndProductId(orderId, productId);
    }

    public void deleteAllProductsInOrderByOrderId(int orderId) {
        productOrderService.deleteAllProductsByOrderId(orderId);
    }
}
