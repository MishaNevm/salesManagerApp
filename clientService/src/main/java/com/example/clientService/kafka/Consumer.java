package com.example.clientService.kafka;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private final ModelMapper modelMapper;

    @Autowired
    public Consumer(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


}
