package com.example.clientService.kafka;

import com.example.clientService.controllers.ClientController;
import com.example.clientService.dto.ClientDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final ObjectMapper objectMapper;
    private final ClientController clientController;

    @Autowired
    public Consumer(ObjectMapper objectMapper, ClientController clientController) {
        this.objectMapper = objectMapper;
        this.clientController = clientController;
    }

    @KafkaListener(topics = "${application.kafka.clientTopicRequest}")
    public void clientRequestTopicListener(ConsumerRecord<Integer, byte[]> consumerRecord){
        int methodCode = consumerRecord.key();
        try {
            switch (methodCode) {
                case 0 -> clientController.findAll();
                case 1 -> clientController.findById(objectMapper.readValue(consumerRecord.value(), ClientDTO.class).getId());
                case 3 -> clientController.create(objectMapper.readValue(consumerRecord.value(), ClientDTO.class));
                case 4 -> clientController.update(objectMapper.readValue(consumerRecord.value(), ClientDTO.class));
                case 5 -> clientController.delete(objectMapper.readValue(consumerRecord.value(), ClientDTO.class).getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
