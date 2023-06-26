package com.example.clientService.repositoryes;

import com.example.clientService.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    List<Bank> findByBikOrCheckingAccount(String bik, String checkingAccount);
}
