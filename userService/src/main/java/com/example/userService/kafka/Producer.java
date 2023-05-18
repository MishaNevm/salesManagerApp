package com.example.userService.kafka;

import com.example.userService.dto.UserDTOResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;
    private final String USER_TOPIC_RESPONSE;

    public Producer(KafkaTemplate<Integer, Object> kafkaTemplate, @Value("${application.kafka.userTopicResponse}") String userTopicResponse) {
        this.kafkaTemplate = kafkaTemplate;

        USER_TOPIC_RESPONSE = userTopicResponse;
    }

    public void sendMessage(Integer methodCode, UserDTOResponse userDTOResponse) {
        kafkaTemplate.send(USER_TOPIC_RESPONSE, methodCode, userDTOResponse);
    }
}
