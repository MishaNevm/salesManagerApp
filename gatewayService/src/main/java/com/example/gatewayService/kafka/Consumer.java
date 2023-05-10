package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.UserDTOResponse;
import com.example.gatewayService.services.UserStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final ObjectMapper objectMapper;
    private final UserStorageService userStorageService;

    @Autowired
    public Consumer(ObjectMapper objectMapper, UserStorageService userStorageService) {
        this.objectMapper = objectMapper;
        this.userStorageService = userStorageService;
    }

    @KafkaListener(topics = "userTopic")
    public void listenUserTopic(ConsumerRecord<Integer, byte[]> consumerRecord) {

        int MethodCode = consumerRecord.key();
        switch (MethodCode) {
            case 0: {
                try {
                    userStorageService.setUserDTO(objectMapper.readValue(consumerRecord.value(), UserDTOResponse.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
