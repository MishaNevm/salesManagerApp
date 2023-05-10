package com.example.userService.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = "userTopic")
    public void userTopicListener (ConsumerRecord<Integer, byte[]> consumerRecord) {

    }
}
