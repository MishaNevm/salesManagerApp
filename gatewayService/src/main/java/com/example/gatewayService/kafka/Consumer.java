package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.CustomResponse;
import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.service.UserStorageService;
import com.example.gatewayService.util.MethodsCodes;
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

    @Autowired
    public Consumer(ObjectMapper objectMapper, UserStorageService userStorageService) {
        this.objectMapper = objectMapper;
        responseMap = new HashMap<>();
        for (MethodsCodes methodsCode : MethodsCodes.values()) {
            if (methodsCode.isHasResponse()) {
                responseMap.put(methodsCode, new ArrayBlockingQueue<>(300));
            }
        }
    }


    @KafkaListener(topics = "userTopicResponse")
    public void listenUserTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {
        try {
            MethodsCodes methodsCodes = MethodsCodes.searchByCode(consumerRecord.key());
            if (methodsCodes != null) {
                responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), UserDTOResponse.class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MethodsCodes, BlockingQueue<CustomResponse<?>>> getResponseMap() {
        return responseMap;
    }
}
