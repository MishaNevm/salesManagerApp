package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final String USER_TOPIC_REQUEST;

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(@Value("${application.kafka.userTopicRequest}") String userTopicRequest, KafkaTemplate<Integer, Object> kafkaTemplate) {
        USER_TOPIC_REQUEST = userTopicRequest;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequestToUserService(MethodsCodes methodsCodes) {
        try {
            kafkaTemplate.send(USER_TOPIC_REQUEST, methodsCodes.getCode(), new UserDTO());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequestToUserService(MethodsCodes methodsCodes, UserDTO userDTO) {
        try {
            kafkaTemplate.send(USER_TOPIC_REQUEST, methodsCodes.getCode(), userDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
