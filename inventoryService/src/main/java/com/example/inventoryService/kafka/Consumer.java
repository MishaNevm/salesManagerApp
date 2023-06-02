package com.example.inventoryService.kafka;

import com.example.inventoryService.controllers.ProductController;
import com.example.inventoryService.dto.ProductDTO;
import com.example.inventoryService.dto.ProductOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final ProductController productController;
    private final ObjectMapper objectMapper;


    @Autowired
    public Consumer(ProductController productController, ObjectMapper objectMapper) {
        this.productController = productController;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${application.kafka.inventoryTopicRequest}")
    public void productTopicRequestListener(ConsumerRecord<Integer, byte[]> consumerRecord) throws IOException {
        int methodCode = consumerRecord.key();
        switch (methodCode) {
            case 26 -> productController.findAll();
            case 27 -> productController.findById(objectMapper.readValue(consumerRecord.value(), ProductDTO.class).getId());
            case 28 -> productController.save(objectMapper.readValue(consumerRecord.value(), ProductDTO.class));
            case 29 -> productController.update(objectMapper.readValue(consumerRecord.value(), ProductDTO.class));
            case 30 -> productController.delete(objectMapper.readValue(consumerRecord.value(), ProductDTO.class).getId());
            case 31 -> productController.addProductToOrder(objectMapper.readValue(consumerRecord.value(), ProductOrderDTO.class));
        }
    }
}
