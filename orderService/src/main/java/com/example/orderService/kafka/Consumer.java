package com.example.orderService.kafka;

import com.example.orderService.controllers.OrderController;
import com.example.orderService.dto.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final ObjectMapper objectMapper;
    private final OrderController orderController;

    @Autowired
    public Consumer(ObjectMapper objectMapper, OrderController orderController) {
        this.objectMapper = objectMapper;
        this.orderController = orderController;
    }

    @KafkaListener(topics = "${application.kafka.orderTopicRequest}")
    public void orderTopicRequestListener(ConsumerRecord<Integer, byte[]> consumerRecord) {
        int methodCode = consumerRecord.key();
        try {
            switch (methodCode) {
                case 20 -> orderController.findAll();
                case 21 -> orderController.findById(objectMapper.readValue(consumerRecord.value(), OrderDTO.class).getId());
                case 23 -> orderController.create(objectMapper.readValue(consumerRecord.value(), OrderDTO.class));
                case 24 -> orderController.update(objectMapper.readValue(consumerRecord.value(), OrderDTO.class));
                case 25 -> orderController.delete(objectMapper.readValue(consumerRecord.value(), OrderDTO.class).getId());
                case 36 -> orderController.findByClientId(objectMapper.readValue(consumerRecord.value(), Integer.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
