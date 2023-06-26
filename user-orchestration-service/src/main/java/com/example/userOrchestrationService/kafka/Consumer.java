package com.example.userOrchestrationService.kafka;

import com.example.userOrchestrationService.dto.*;
import com.example.userOrchestrationService.util.CustomResponse;
import com.example.userOrchestrationService.util.ErrorResponse;
import com.example.userOrchestrationService.util.MethodsCodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class Consumer {

    private final ObjectMapper objectMapper;

    private final Map<MethodsCodes, BlockingQueue<CustomResponse<?>>> responseMap;
    private final Map<MethodsCodes, BlockingQueue<ErrorResponse>> errorResponseMap;

    @Autowired
    public Consumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        responseMap = new HashMap<>();
        errorResponseMap = new HashMap<>();
        for (MethodsCodes methodsCode : MethodsCodes.values()) {
            if (methodsCode.isHasModelResponse()) {
                responseMap.put(methodsCode, new ArrayBlockingQueue<>(100));
            }
            if (methodsCode.isHasErrorResponse()) {
                errorResponseMap.put(methodsCode, new ArrayBlockingQueue<>(100));
            }
        }
    }

    @KafkaListener(topics = "${application.kafka.clientTopicResponse}")
    public void listenClientTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {
        try {
            MethodsCodes methodsCodes = MethodsCodes.searchByCode(consumerRecord.key());
            if (methodsCodes != null) {
                if (methodsCodes.isHasModelResponse()) {
                    if (methodsCodes.getCode() < MethodsCodes.GET_ALL_BANKS.getCode()) {
                        responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), ClientDTOResponse.class));
                    } else {
                        responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), BankDTOResponse.class));
                    }
                } else {
                    errorResponseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), ErrorResponse.class));
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.orderTopicResponse}")
    public void listenOrderTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {
        try {
            MethodsCodes methodsCodes = MethodsCodes.searchByCode(consumerRecord.key());
            if (methodsCodes != null) {
                responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), OrderDTOResponse.class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.inventoryTopicResponse}")
    public void listenInventoryTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {
        try {
            MethodsCodes methodsCodes = MethodsCodes.searchByCode(consumerRecord.key());
            if (methodsCodes != null) {
                if (methodsCodes.isHasErrorResponse()) {
                    errorResponseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), ErrorResponse.class));
                } else {
                    responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), ProductDTOResponse.class));
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MethodsCodes, BlockingQueue<CustomResponse<?>>> getResponseMap() {
        return responseMap;
    }

    public Map<MethodsCodes, BlockingQueue<ErrorResponse>> getErrorResponseMap() {
        return errorResponseMap;
    }
}
