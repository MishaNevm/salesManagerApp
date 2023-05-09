package com.example.bankService.services;

import com.example.bankService.models.Bank;
import com.example.bankService.repositoryes.BankRepository;
import com.example.bankService.util.BankNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BankService {

    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> findAll () {
        return bankRepository.findAll();
    }

    public Bank findById (int id) {
        return bankRepository.findById(id).orElseThrow(BankNotFoundException::new);
    }

    @Transactional
    public void save (Bank bank) {
        bank.setCreated_at(new Date());
        bankRepository.save(bank);
    }

    @Transactional
    public void update (Bank bank) {
        bank.setUpdated_at(new Date());
        bankRepository.save(bank);
    }

    @Transactional
    public void delete (int id) {
        bankRepository.deleteById(id);
    }


}
