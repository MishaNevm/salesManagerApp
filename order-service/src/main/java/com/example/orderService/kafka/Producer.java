package com.example.orderService.kafka;

import com.example.orderService.dto.OrderDTOResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class Producer {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;
    private final String ORDER_TOPIC_RESPONSE;

    public Producer(KafkaTemplate<Integer, Object> kafkaTemplate, @Value("${application.kafka.orderTopicResponse}") String orderTopicResponse) {
        this.kafkaTemplate = kafkaTemplate;
        ORDER_TOPIC_RESPONSE = orderTopicResponse;
    }

    public void sendMessageToOrderResponseTopic(Integer methodCode, OrderDTOResponse orderDTOResponse) {
        kafkaTemplate.send(ORDER_TOPIC_RESPONSE, methodCode, orderDTOResponse);
    }

}
