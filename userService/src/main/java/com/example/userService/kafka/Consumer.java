package com.example.userService.kafka;

import com.example.userService.controllers.UserController;
import com.example.userService.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final UserController userController;
    private final ObjectMapper objectMapper;

    @Autowired
    public Consumer(UserController userController, ObjectMapper objectMapper) {
        this.userController = userController;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "userTopic")
    public void userTopicListener(ConsumerRecord<Integer, byte[]> consumerRecord) {
        int methodCode = consumerRecord.key();
        try {
            switch (methodCode) {
                case 0: {
                    userController.findAll();
                    break;
                }
                case 1: {
                    userController.findById(objectMapper.readValue(consumerRecord.value(), UserDTO.class).getId());
                    break;
                }
                case 2: {
                    userController.findByEmail(objectMapper.readValue(consumerRecord.value(), UserDTO.class).getEmail());
                    break;
                }
                case 3:{
                    userController.create(objectMapper.readValue(consumerRecord.value(), UserDTO.class));
                    break;
                }
                case 4: {
                    userController.update(objectMapper.readValue(consumerRecord.value(), UserDTO.class));
                    break;
                }
                case 5: {
                    userController.delete(objectMapper.readValue(consumerRecord.value(), UserDTO.class).getId());
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
