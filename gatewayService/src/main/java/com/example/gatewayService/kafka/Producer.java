package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.*;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final String CLIENT_TOPIC_REQUEST;
    private final String ORDER_TOPIC_REQUEST;
    private final String INVENTORY_TOPIC_REQUEST;
    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(@Value("${application.kafka.clientTopicRequest}") String clientTopicRequest,
                    @Value("${application.kafka.orderTopicRequest}") String orderTopicRequest,
                    @Value("${application.kafka.inventoryTopicRequest}") String inventoryTopicRequest, KafkaTemplate<Integer, Object> kafkaTemplate) {
        CLIENT_TOPIC_REQUEST = clientTopicRequest;
        ORDER_TOPIC_REQUEST = orderTopicRequest;
        INVENTORY_TOPIC_REQUEST = inventoryTopicRequest;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequestToClientService(MethodsCodes methodsCodes, ClientDTO clientDTO) {
        try {
            kafkaTemplate.send(CLIENT_TOPIC_REQUEST, methodsCodes.getCode(), clientDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToClientService(MethodsCodes methodsCodes, BankDTO bankDTO) {
        try {
            kafkaTemplate.send(CLIENT_TOPIC_REQUEST, methodsCodes.getCode(), bankDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToOrderService(MethodsCodes methodsCodes, OrderDTO orderDTO) {
        try {
            kafkaTemplate.send(ORDER_TOPIC_REQUEST, methodsCodes.getCode(), orderDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToOrderService(MethodsCodes methodsCodes, String clientShortName) {
        try {
            kafkaTemplate.send(ORDER_TOPIC_REQUEST, methodsCodes.getCode(), clientShortName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToInventoryService(MethodsCodes methodsCodes, ProductDTO productDTO) {
        try {
            kafkaTemplate.send(INVENTORY_TOPIC_REQUEST, methodsCodes.getCode(), productDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToInventoryService(MethodsCodes methodsCodes, ProductOrderDTO productOrderDTO) {
        try {
            kafkaTemplate.send(INVENTORY_TOPIC_REQUEST, methodsCodes.getCode(), productOrderDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
