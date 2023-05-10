package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final String USER_TOPIC = "userTopic";

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(KafkaTemplate<Integer, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendORequestToUserService(MethodsCodes methodsCodes) {
        try {
            kafkaTemplate.send(USER_TOPIC, methodsCodes.getCode(), null);
        } catch (Exception e) {
            return "ERROR";
        }
        return "OK";
    }

    public String sendORequestToUserService(MethodsCodes methodsCodes, UserDTO userDTO) {
        try {
            kafkaTemplate.send("userTopic", methodsCodes.getCode(), userDTO);
        } catch (Exception e) {
            return "ERROR";
        }
        return "OK";
    }
}
