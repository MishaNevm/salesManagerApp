package com.example.clientService.util;

import com.example.clientService.dto.*;
import com.example.clientService.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil( ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }



    public Client convertClientDTOToClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        if (clientDTO.getBankDTO() != null) {
            client.setBank(modelMapper.map(clientDTO.getBankDTO(), Bank.class));
        }
        return client;
    }

    public ClientDTO convertClientToClientDTO(Client client) {
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        if (client.getBank() != null) {
            clientDTO.setBankDTO(modelMapper.map(client.getBank(), BankDTO.class));
        }
        return clientDTO;
    }

    public Bank convertBankDTOToBank(BankDTO bankDTO) {
        Bank bank = modelMapper.map(bankDTO, Bank.class);
        if (bankDTO.getClientDTO() != null) {
            bank.setClient(modelMapper.map(bankDTO.getClientDTO(), Client.class));
        }
        return bank;
    }

    public BankDTO convertBankToBankDTO(Bank bank) {
        BankDTO bankDTO = modelMapper.map(bank, BankDTO.class);
        if (bank.getClient() != null) {
            bankDTO.setClientDTO(modelMapper.map(bank.getClient(), ClientDTO.class));
        }
        return bankDTO;
    }


}
