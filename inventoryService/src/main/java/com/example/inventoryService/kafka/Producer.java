package com.example.inventoryService.kafka;

import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.dto.ProductOrderDTOResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final String PRODUCT_TOPIC_RESPONSE;

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(@Value("${application.kafka.inventoryTopicResponse}") String productTopicResponse, KafkaTemplate<Integer, Object> kafkaTemplate) {
        PRODUCT_TOPIC_RESPONSE = productTopicResponse;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToInventoryResponseTopic(Integer methodCode, ProductDTOResponse productDTOResponse){
        kafkaTemplate.send(PRODUCT_TOPIC_RESPONSE, methodCode, productDTOResponse);
    }
    public void sendMessageToInventoryResponseTopic(Integer methodCode, ProductOrderDTOResponse productOrderDTOResponse){
        kafkaTemplate.send(PRODUCT_TOPIC_RESPONSE, methodCode, productOrderDTOResponse);
    }
}
