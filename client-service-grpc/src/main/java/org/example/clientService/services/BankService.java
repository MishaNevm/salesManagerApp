package org.example.clientService.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.clientService.BankServiceGrpc;
import org.example.clientService.BankServiceOuterClass;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getAll(Empty request,
                       StreamObserver<BankServiceOuterClass.BankResponse> responseObserver) {

    }

    @Override
    public void getById(BankServiceOuterClass.Id request,
                        StreamObserver<BankServiceOuterClass.Bank> responseObserver) {

    }

    @Override
    public void create(BankServiceOuterClass.Bank request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {
    }

    @Override
    public void update(BankServiceOuterClass.Bank request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {

    }

    @Override
    public void delete(BankServiceOuterClass.Id request,
                       StreamObserver<BankServiceOuterClass.Result> responseObserver) {

    }

}
