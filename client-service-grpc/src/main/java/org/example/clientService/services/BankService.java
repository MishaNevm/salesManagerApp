package org.example.clientService.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.clientService.BankServiceGrpc;
import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.dao.BankDAO;
import org.example.clientService.mappers.ModelsMapper;
import org.example.clientService.models.Bank;
import org.example.clientService.util.BankUniqueValidator;

import java.util.List;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private final ModelsMapper mapper;
    private final BankDAO bankDAO;

    private final BankUniqueValidator bankUniqueValidator;

    public BankService() {
        mapper = ModelsMapper.INSTANCE;
        bankDAO = BankDAO.getInstance();
        bankUniqueValidator = new BankUniqueValidator();
    }


    @Override
    public void getAll(Empty request,
                       StreamObserver<BankServiceOuterClass.BankResponse> responseObserver) {
        List<BankServiceOuterClass.Bank> banks = bankDAO.findAll().stream().map(a ->
                mapper.convertBankToOuterBank(a).setClientId(a.getClient().getId()).build()).toList();
        responseObserver.onNext(BankServiceOuterClass.BankResponse.newBuilder().addAllBanks(banks).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getById(BankServiceOuterClass.Id request,
                        StreamObserver<BankServiceOuterClass.Bank> responseObserver) {
        responseObserver.onNext(mapper.convertBankToOuterBank(bankDAO.findById(request.getId())).build());
        responseObserver.onCompleted();
    }

    @Override
    public void create(BankServiceOuterClass.Bank request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {
        BankServiceOuterClass.ErrorResponse.Builder errorResponse = BankServiceOuterClass.ErrorResponse.newBuilder();
        bankUniqueValidator.validate(mapper.convertOuterBankToBank(request), errorResponse);
        if (errorResponse.getErrorsList().isEmpty()) {
            bankDAO.save(mapper.convertOuterBankToBank(request));
        }
        responseObserver.onNext(errorResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(BankServiceOuterClass.Bank request,
                       StreamObserver<BankServiceOuterClass.ErrorResponse> responseObserver) {
        BankServiceOuterClass.ErrorResponse.Builder errorResponse = BankServiceOuterClass.ErrorResponse.newBuilder();
        Bank bank = mapper.convertOuterBankToBank(request);
        bankUniqueValidator.validate(bank, errorResponse);
        if (errorResponse.getErrorsList().isEmpty()) {
            bankDAO.update(bank);
        }
        responseObserver.onNext(errorResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(BankServiceOuterClass.Id request,
                       StreamObserver<BankServiceOuterClass.Result> responseObserver) {
        responseObserver.onNext(BankServiceOuterClass.Result.newBuilder().setResult(bankDAO.delete(request.getId())).build());
        responseObserver.onCompleted();
    }
}
