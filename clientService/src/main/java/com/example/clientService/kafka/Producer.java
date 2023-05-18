package com.example.clientService.kafka;

import com.example.clientService.dto.ClientDTOResponse;
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

    public void sendMessageToClientTopicResponse(Integer methodCode, ClientDTOResponse clientDTOResponse) {
        kafkaTemplate.send(CLIENT_TOPIC_RESPONSE, methodCode, clientDTOResponse);
    }

}
