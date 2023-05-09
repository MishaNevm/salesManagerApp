package com.example.bankService.repositoryes;

import com.example.bankService.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    Optional<Bank> findByBik (String bik);
    Optional<Bank> findByCheckingAccount (String checkingAccount);
}
