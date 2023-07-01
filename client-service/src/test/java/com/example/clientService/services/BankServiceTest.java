package com.example.clientService.services;

import com.example.clientService.dto.BankDTO;
import com.example.clientService.dto.BankDTOResponse;
import com.example.clientService.models.Bank;
import com.example.clientService.repositoryes.BankRepository;
import com.example.clientService.util.ModelMapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BankServiceTest {

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private BankService bankService;

    @Test
    public void testFindAll() {

        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank());
        banks.add(new Bank());


        doReturn(banks).when(bankRepository).findAll();
        doReturn(new BankDTO()).when(modelMapperUtil).convertBankToBankDTO(any(Bank.class));


        BankDTOResponse response = bankService.findAll();


        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertEquals(banks.size(), response.getResponse().size());


        verify(bankRepository, times(1)).findAll();
        verify(modelMapperUtil, times(banks.size())).convertBankToBankDTO(any(Bank.class));
    }

    @Test
    public void testFindById() {

        int id = 1;
        Bank bank = new Bank();
        bank.setId(id);


        doReturn(Optional.of(bank)).when(bankRepository).findById(id);
        doReturn(new BankDTO()).when(modelMapperUtil).convertBankToBankDTO(bank);


        BankDTOResponse response = bankService.findById(id);


        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertEquals(1, response.getResponse().size());


        verify(bankRepository, times(1)).findById(id);
        verify(modelMapperUtil, times(1)).convertBankToBankDTO(bank);
    }

    @Test
    public void testSave() {

        BankDTO bankDTO = new BankDTO();

        doReturn(new Bank()).when(modelMapperUtil).convertBankDTOToBank(bankDTO);

        bankService.save(bankDTO);


        verify(modelMapperUtil, times(1)).convertBankDTOToBank(bankDTO);
        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    public void testUpdate() {

        int id = 1;
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId(id);
        bankDTO.setCreatedAt(new Date());

        Bank bank = new Bank();
        bank.setCreatedAt(new Date());

        doReturn(Optional.of(bank)).when(bankRepository).findById(id);
        doReturn(bank).when(modelMapperUtil).convertBankDTOToBank(bankDTO);


        bankService.update(bankDTO);


        verify(bankRepository, times(1)).findById(id);
        verify(modelMapperUtil, times(1)).convertBankDTOToBank(bankDTO);
        verify(bankRepository, times(1)).save(bank);
    }

    @Test
    public void testDelete() {

        int id = 1;
        Bank bank = new Bank();

        doReturn(Optional.of(bank)).when(bankRepository).findById(id);

        bankService.delete(id);

        verify(bankRepository, times(1)).findById(id);
        verify(bankRepository, times(1)).deleteById(id);
    }
}
