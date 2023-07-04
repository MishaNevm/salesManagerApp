package org.example.clientService.mappers;

import org.example.clientService.BankServiceOuterClass;
import org.example.clientService.models.Bank;
import org.example.clientService.models.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

class MapperTest {


    @Test
    void convertBankToOuterBank(){

        Bank bank = new Bank();

        bank.setId(5);
        bank.setBik("123456789");
        bank.setCity("Test");
        bank.setCountry(bank.getCity());
        bank.setCheckingAccount(bank.getBik());
        bank.setCorporateAccount(bank.getBik());
        bank.setName("Test");
        Client client = new Client();
        client.setId(1);
        bank.setClient(client);



        ModelsMapper mapper = ModelsMapper.INSTANCE;

        BankServiceOuterClass.Bank.Builder builder =  mapper.convertBankToOuterBank(bank);
        builder.setClientId(bank.getClient().getId());
        BankServiceOuterClass.Bank bank1 = builder.build();
        System.out.println(bank1.getClientId());
        System.out.println(builder.getCreatedAt());
        Bank bank2 = mapper.convertOuterBankToBank(bank1);
        System.out.println(bank2.getId());
        System.out.println(bank2.getCreatedAt());

    }

    @Test
    void convertClientToOuterClient() {




    }
}