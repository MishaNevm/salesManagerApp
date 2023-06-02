package com.example.clientService.kafka;

import com.example.clientService.controllers.BankController;
import com.example.clientService.controllers.ClientController;
import com.example.clientService.dto.BankDTO;
import com.example.clientService.dto.ClientDTO;
import com.example.clientService.dto.ClientDTOResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private final ObjectMapper objectMapper;
    private final ClientController clientController;

    private final BankController bankController;

    @Autowired
    public Consumer(ObjectMapper objectMapper, ClientController clientController, BankController bankController) {
        this.objectMapper = objectMapper;
        this.clientController = clientController;
        this.bankController = bankController;
    }

    @KafkaListener(topics = "${application.kafka.clientTopicRequest}")
    public void clientRequestTopicListener(ConsumerRecord<Integer, byte[]> consumerRecord) {
        int methodCode = consumerRecord.key();
        try {
            switch (methodCode) {
                case 6 -> clientController.findAll();
                case 7 ->
                        clientController.findById(objectMapper.readValue(consumerRecord.value(), ClientDTO.class).getId());
                case 8 -> {clientController.create(objectMapper.readValue(consumerRecord.value(), ClientDTO.class));}
                case 9 -> clientController.update(objectMapper.readValue(consumerRecord.value(), ClientDTO.class));
                case 10 ->
                        clientController.delete(objectMapper.readValue(consumerRecord.value(), ClientDTO.class));
                case 15 -> bankController.findAll();
                case 16 ->
                        bankController.findById(objectMapper.readValue(consumerRecord.value(), BankDTO.class).getId());
                case 17 -> {
                    bankController.create(objectMapper.readValue(consumerRecord.value(), BankDTO.class));
                }
                case 18 -> {
                    bankController.update(objectMapper.readValue(consumerRecord.value(), BankDTO.class));
                }
                case 19 -> {
                    bankController.delete(objectMapper.readValue(consumerRecord.value(), BankDTO.class).getId());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
