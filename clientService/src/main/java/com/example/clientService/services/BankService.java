package com.example.clientService.services;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.dto.BankDTOResponse;
import com.example.clientService.models.Bank;
import com.example.clientService.repositoryes.BankRepository;
import com.example.clientService.util.ModelMapperUtil;
import com.example.clientService.util.bankUtil.BankNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class BankService {

    private final BankRepository bankRepository;
    private final ModelMapperUtil modelMapperUtil;
    private BankDTOResponse bankDTOResponse;

    @Autowired
    public BankService(BankRepository bankRepository, ModelMapperUtil modelMapperUtil) {
        this.bankRepository = bankRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    public BankDTOResponse findAll () {
        bankDTOResponse = new BankDTOResponse();
        bankDTOResponse.setResponse(bankRepository.findAll().stream().map(modelMapperUtil::convertBankToBankDTO).toList());
        return bankDTOResponse;
    }

    public BankDTOResponse findById (int id) {
        bankDTOResponse = new BankDTOResponse();
        bankDTOResponse
                .setResponse(Collections
                        .singletonList(modelMapperUtil
                                .convertBankToBankDTO(bankRepository
                                        .findById(id)
                                        .orElseThrow(BankNotFoundException::new))));
        return bankDTOResponse;
    }

    @Transactional
    public void save (BankDTO bankDTO) {
        Bank bank = modelMapperUtil.convertBankDTOToBank(bankDTO);
        bank.setCreated_at(new Date());
        bankRepository.save(bank);
    }

    @Transactional
    public void update (BankDTO bankDTO) {
        Bank bank = modelMapperUtil.convertBankDTOToBank(bankDTO);
        bank.setUpdated_at(new Date());
        bankRepository.save(bank);
    }

    @Transactional
    public void delete (int id) {
        bankRepository.deleteById(id);
    }


}
