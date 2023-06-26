package com.example.clientService.kafka;

import com.example.clientService.dto.BankDTOResponse;
import com.example.clientService.dto.ClientDTOResponse;
import com.example.clientService.util.ErrorResponse;
import com.example.clientService.util.MethodsCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;
    private final String CLIENT_TOPIC_RESPONSE;

    public Producer(KafkaTemplate<Integer, Object> kafkaTemplate, @Value("${application.kafka.clientTopicResponse}") String clientTopicResponse) {
        this.kafkaTemplate = kafkaTemplate;
        CLIENT_TOPIC_RESPONSE = clientTopicResponse;
    }

    public void sendMessageToClientTopicResponse(MethodsCodes methodCode, ClientDTOResponse clientDTOResponse) {
        kafkaTemplate.send(CLIENT_TOPIC_RESPONSE, methodCode.getCode(), clientDTOResponse);
    }
    public void sendMessageToClientTopicResponse(MethodsCodes methodCode, BankDTOResponse bankDTOResponse) {
        kafkaTemplate.send(CLIENT_TOPIC_RESPONSE, methodCode.getCode(), bankDTOResponse);
    }

    public void sendMessageToClientTopicResponse(MethodsCodes methodCode, ErrorResponse errorResponse) {
        kafkaTemplate.send(CLIENT_TOPIC_RESPONSE, methodCode.getCode(), errorResponse);
    }

}
