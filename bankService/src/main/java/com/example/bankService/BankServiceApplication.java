package com.example.bankService;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAdminServer
public class BankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankServiceApplication.class, args);
        org.apache.kafka.common.serialization.StringDeserializer stringDeserializer = new StringDeserializer();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
