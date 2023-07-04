package org.example.clientService;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.clientService.services.BankService;
import org.example.clientService.services.ClientService;

import java.io.IOException;

public class ClientServiceApplication {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder
                .forPort(8383)
                .addService(new BankService())
                .addService(new ClientService())
                .build();
        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }
}
