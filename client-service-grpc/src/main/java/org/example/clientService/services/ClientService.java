package org.example.clientService.services;

import io.grpc.stub.StreamObserver;
import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.ClientServiceGrpc;
import org.example.clientService.ClientServiceOuterClass;

public class ClientService extends ClientServiceGrpc.ClientServiceImplBase {

    @Override
    public void getAll(com.google.protobuf.Empty request,
                       StreamObserver<ClientServiceOuterClass.ClientResponse> responseObserver) {

    }

    @Override
    public void getById(BankServiceOuterClass.Id request,
                        StreamObserver<ClientServiceOuterClass.Client> responseObserver) {

    }

    @Override
    public void create(ClientServiceOuterClass.Client request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {

    }

   @Override
    public void update(ClientServiceOuterClass.Client request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {

    }

    @Override
    public void delete(BankServiceOuterClass.Id request,
                       StreamObserver<BankServiceOuterClass.Result> responseObserver) {


    }
}
