package org.example.clientService;


import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ClientServiceApplication {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(8383).build();
        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }
}
