package com.example.clientService.services;

import com.example.clientService.dto.ClientDTO;
import com.example.clientService.dto.ClientDTOResponse;
import com.example.clientService.models.Client;
import com.example.clientService.repositoryes.ClientRepository;
import com.example.clientService.util.ModelMapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class ClientServiceTest {
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private ModelMapperUtil modelMapper;

    @Autowired
    private ClientService clientService;


    @Test
    void testFindAll() {

        List<Client> clients = Collections.singletonList(new Client());
        when(clientRepository.findAll()).thenReturn(clients);


        ClientDTOResponse response = clientService.findAll();

        assertEquals(clients.size(), response.getResponse().size());


        verify(clientRepository, times(1)).findAll();
        verify(modelMapper, times(clients.size())).convertClientToClientDTO(any(Client.class));
    }

    @Test
    void testFindById() {

        int id = 1;
        Client client = new Client();
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));


        ClientDTOResponse response = clientService.findById(id);


        assertEquals(1, response.getResponse().size());


        verify(clientRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).convertClientToClientDTO(client);
    }

    @Test
    void testSave() {

        ClientDTO clientDTO = new ClientDTO();
        Client client = new Client();
        when(modelMapper.convertClientDTOToClient(clientDTO)).thenReturn(client);


        clientService.save(clientDTO);


        verify(modelMapper, times(1)).convertClientDTOToClient(clientDTO);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testUpdate() {

        int id = 1;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(id);
        Client clientFromDB = new Client();
        clientFromDB.setCreatedAt(new Date());
        when(clientRepository.findById(id)).thenReturn(Optional.of(clientFromDB));
        when(modelMapper.convertClientDTOToClient(clientDTO)).thenReturn(clientFromDB);


        clientService.update(clientDTO);


        verify(clientRepository, times(1)).findById(id);
        verify(modelMapper, times(1)).convertClientDTOToClient(clientDTO);
        verify(clientRepository, times(1)).save(clientFromDB);
    }

    @Test
    void testDelete() {

        int id = 1;
        when(clientRepository.findById(id)).thenReturn(Optional.of(new Client()));


        clientService.delete(id);


        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).deleteById(id);
    }
}
