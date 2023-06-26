package com.example.inventoryService.kafka;

import com.example.inventoryService.dto.ProductDTOResponse;
import com.example.inventoryService.util.ErrorResponse;
import com.example.inventoryService.util.MethodsCodes;
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

    public void sendMessageToInventoryResponseTopic(MethodsCodes methodsCodes, ProductDTOResponse productDTOResponse){
        kafkaTemplate.send(PRODUCT_TOPIC_RESPONSE, methodsCodes.getCode(), productDTOResponse);
    }

    public void sendMessageToInventoryResponseTopic (MethodsCodes methodsCodes, ErrorResponse errorResponse) {
        kafkaTemplate.send(PRODUCT_TOPIC_RESPONSE, methodsCodes.getCode(), errorResponse);
    }
}
