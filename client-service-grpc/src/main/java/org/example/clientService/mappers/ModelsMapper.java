package org.example.clientService.mappers;


import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.ClientServiceOuterClass;
import org.example.clientService.models.Bank;
import org.example.clientService.models.Client;
import org.example.clientService.util.ClientTypes;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ModelsMapper {
    ModelsMapper INSTANCE = Mappers.getMapper(ModelsMapper.class);

    BankServiceOuterClass.Bank.Builder convertBankToOuterBank(Bank bank);


    @Mapping(target = "client", ignore = true)
    Bank convertOuterBankToBank(BankServiceOuterClass.Bank bank);

    ClientServiceOuterClass.Client.Builder convertClientToOuterClient(Client client);

    @Mapping(target = "type", expression = "java(convertOuterClientTypeToClientType(client.getType()))")
    Client convertOuterClientToClient(ClientServiceOuterClass.Client client);

    default ClientServiceOuterClass.Client.Builder clientBuilder() {
        return ClientServiceOuterClass.Client.newBuilder();
    }

    default ClientTypes convertOuterClientTypeToClientType(ClientServiceOuterClass.ClientTypes outerClientType) {
        if (outerClientType != null) {
            ClientTypes[] clientTypes = ClientTypes.values();
            for (ClientTypes clientType : clientTypes) {
                if (clientType.name().equals(outerClientType.name())) {
                    return clientType;
                }
            }
        }
        return null;
    }

    default BankServiceOuterClass.Bank.Builder bankBuilder() {
        return BankServiceOuterClass.Bank.newBuilder();
    }

    default Date parseDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
