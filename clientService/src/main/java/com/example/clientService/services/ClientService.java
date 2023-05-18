package com.example.clientService.services;


import com.example.clientService.dto.ClientDTOResponse;
import com.example.clientService.models.Client;
import com.example.clientService.util.ModelMapperUtil;
import com.example.clientService.util.clientUtil.ClientNotFoundException;
import com.example.clientService.repositoryes.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapperUtil modelMapperUtil;

    @Autowired
    public ClientService(ClientRepository clientRepository, ModelMapperUtil modelMapperUtil) {
        this.clientRepository = clientRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    public ClientDTOResponse findAll() {
        ClientDTOResponse clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse.setResponse(clientRepository.findAll().stream().map(modelMapperUtil::convertClientToClientDTO).toList());
        return clientDTOResponse;
    }

    public ClientDTOResponse findById(int id) {
        ClientDTOResponse clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse.setResponse(Collections.singletonList(modelMapperUtil.convertClientToClientDTO(clientRepository.findById(id).orElseThrow(ClientNotFoundException::new))));
        return clientDTOResponse;
    }

    public Client findByShortName(String shortName) {
        return clientRepository.findByShortName(shortName).orElseThrow(ClientNotFoundException::new);
    }

    public Optional<Client> findByInn(Client client) {
        return clientRepository.findByInn(client.getInn());
    }

    public Optional<Client> findByKpp(Client client) {
        return clientRepository.findByKpp(client.getKpp());
    }

    public Optional<Client> findByOgrn(Client client) {
        return clientRepository.findByOgrn(client.getOgrn());
    }


    @Transactional
    public void save(Client client) {
        client.setCreatedAt(new Date());
        clientRepository.save(client);
    }


    @Transactional
    public void update(Client client) {
        client.setUpdatedAt(new Date());
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        clientRepository.deleteById(id);
    }

}
