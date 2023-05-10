package com.example.userService.kafka;

import com.example.userService.dto.UserDTOResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(KafkaTemplate<Integer, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(int methodCode) {
        kafkaTemplate.send("userTopic", methodCode, null);
    }

    public void sendMessage(int methodCode, UserDTOResponse userDTOResponse) {
        kafkaTemplate.send("userTopic", methodCode, userDTOResponse);
    }
}
