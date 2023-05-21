package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.BankDTOResponse;
import com.example.gatewayService.dto.ClientDTOResponse;
import com.example.gatewayService.dto.CustomResponse;
import com.example.gatewayService.dto.UserDTOResponse;
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
    public Consumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        responseMap = new HashMap<>();
        for (MethodsCodes methodsCode : MethodsCodes.values()) {
            if (methodsCode.isHasResponse()) {
                responseMap.put(methodsCode, new ArrayBlockingQueue<>(300));
            }
        }
    }


    @KafkaListener(topics = "${application.kafka.userTopicResponse}")
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

    @KafkaListener(topics = "${application.kafka.clientTopicResponse}")
    public void listenClientTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {
        try {
            MethodsCodes methodsCodes = MethodsCodes.searchByCode(consumerRecord.key());
            if (methodsCodes != null) {
                if (methodsCodes.getCode() < MethodsCodes.GET_ALL_BANKS.getCode()) {
                    responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), ClientDTOResponse.class));
                } else responseMap.get(methodsCodes).put(objectMapper.readValue(consumerRecord.value(), BankDTOResponse.class));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MethodsCodes, BlockingQueue<CustomResponse<?>>> getResponseMap() {
        return responseMap;
    }
}
