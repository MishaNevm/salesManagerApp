package com.example.bankService.util;

import com.example.bankService.dto.BankDTO;
import com.example.bankService.models.Bank;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil( ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }



    public Bank convertBankDTOToBank(BankDTO bankDTO) {
        Bank bank = modelMapper.map(bankDTO, Bank.class);
//        if (bankDTO.getClientDTO() != null) {
//            bank.setClient(modelMapper.map(bankDTO.getClientDTO(), Client.class));
//        }
        return bank;
    }

    public BankDTO convertBankToBankDTO(Bank bank) {
        BankDTO bankDTO = modelMapper.map(bank, BankDTO.class);
//        if (bank.getClient() != null) {
//            bankDTO.setClientDTO(modelMapper.map(bank.getClient(), ClientDTO.class));
//        }
        return bankDTO;
    }
}
