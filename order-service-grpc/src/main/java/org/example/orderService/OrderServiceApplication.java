package org.example.orderService;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.orderService.services.OrderService;

import java.io.IOException;

public class OrderServiceApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8282).addService(new OrderService()).build();
        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }
}
