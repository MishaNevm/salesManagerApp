package org.example.clientService.mappers;

import org.example.clientService.ClientServiceOuterClass;
import org.example.clientService.models.Bank;
import org.example.clientService.models.Client;
import org.example.clientService.util.ClientTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    ModelsMapper clientMapper;

    @BeforeEach
    public void before() {
        clientMapper = ModelsMapper.INSTANCE;
    }

    @Test
    void convertClientToOuterClient() {

        Client client = new Client();
        client.setType(ClientTypes.IP);

        List<Bank> bankList = new ArrayList<>();

        for (int i = 1; i < 3; i++) {
            Bank bank = new Bank();
            bank.setId(i);
            bankList.add(bank);
        }

        client.setBanks(bankList);

        ClientServiceOuterClass.Client.Builder builder = clientMapper.convertClientToOuterClient(client);
        assertEquals(client.getType().name(), builder.getType().name());
        assertNotNull(builder.getBanksList());
        System.out.println(builder.getBanksList().size());
    }

    @Test
    void convertOuterClientToClient() {

        Client client = new Client();
        client.setType(ClientTypes.IP);

        List<Bank> bankList = new ArrayList<>();

        for (int i = 1; i < 3; i++) {
            Bank bank = new Bank();
            bank.setId(i);
            bankList.add(bank);
        }

        client.setBanks(bankList);


        ClientServiceOuterClass.Client client2 = clientMapper.convertClientToOuterClient(client).build();

        assertEquals(client.getType().name(), clientMapper.convertOuterClientToClient(client2).getType().name());

        assertNotNull(client2.getBanksList());
    }
}