package org.example.clientService.services;

import io.grpc.stub.StreamObserver;
import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.ClientServiceGrpc;
import org.example.clientService.ClientServiceOuterClass;
import org.example.clientService.dao.ClientDAO;
import org.example.clientService.mappers.ModelsMapper;

import java.util.List;

public class ClientService extends ClientServiceGrpc.ClientServiceImplBase {

    private final ModelsMapper mapper;
    private final ClientDAO clientDAO;

    public ClientService() {
        mapper = ModelsMapper.INSTANCE;
        clientDAO = ClientDAO.getInstance();
    }


    @Override
    public void getAll(com.google.protobuf.Empty request,
                       StreamObserver<ClientServiceOuterClass.ClientResponse> responseObserver) {
        List<ClientServiceOuterClass.Client> clients = clientDAO.findAll().stream().map(a -> mapper.convertClientToOuterClient(a).build()).toList();
        responseObserver.onNext(ClientServiceOuterClass.ClientResponse.newBuilder().addAllClients(clients).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getById(BankServiceOuterClass.Id request,
                        StreamObserver<ClientServiceOuterClass.Client> responseObserver) {
        responseObserver.onNext(mapper.convertClientToOuterClient(clientDAO.findById(request.getId())).build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(ClientServiceOuterClass.Client request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {
        clientDAO.save(mapper.convertOuterClientToClient(request));
        responseObserver.onCompleted();
    }

    @Override
    public void update(ClientServiceOuterClass.Client request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {
        clientDAO.update(mapper.convertOuterClientToClient(request));
        responseObserver.onCompleted();
    }

    @Override
    public void delete(BankServiceOuterClass.Id request,
                       StreamObserver<BankServiceOuterClass.Result> responseObserver) {
        clientDAO.delete(request.getId());
        responseObserver.onCompleted();
    }
}
