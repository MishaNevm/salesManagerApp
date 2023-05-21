package com.example.gatewayService.kafka;

import com.example.gatewayService.dto.BankDTO;
import com.example.gatewayService.dto.ClientDTO;
import com.example.gatewayService.dto.UserDTO;
import com.example.gatewayService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final String USER_TOPIC_REQUEST;
    private final String CLIENT_TOPIC_REQUEST;
    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    public Producer(@Value("${application.kafka.userTopicRequest}") String userTopicRequest,
                    @Value("${application.kafka.clientTopicRequest}") String clientTopicRequest,
                    KafkaTemplate<Integer, Object> kafkaTemplate) {
        USER_TOPIC_REQUEST = userTopicRequest;
        CLIENT_TOPIC_REQUEST = clientTopicRequest;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRequestToUserService(MethodsCodes methodsCodes, UserDTO userDTO) {
        try {
            kafkaTemplate.send(USER_TOPIC_REQUEST, methodsCodes.getCode(), userDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
