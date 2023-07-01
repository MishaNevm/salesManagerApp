package com.example.clientService.services;


import com.example.clientService.dto.ClientDTO;
import com.example.clientService.dto.ClientDTOResponse;
import com.example.clientService.models.Client;
import com.example.clientService.repositoryes.ClientRepository;
import com.example.clientService.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapperUtil modelMapperUtil;
    private final ModelMapperUtil modelMapper;
    private ClientDTOResponse clientDTOResponse;

    @Autowired
    public ClientService(ClientRepository clientRepository, ModelMapperUtil modelMapperUtil, ModelMapperUtil modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapperUtil = modelMapperUtil;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public ClientDTOResponse findAll() {
        clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse
                .setResponse(clientRepository.findAll()
                        .stream()
                        .map(modelMapperUtil::convertClientToClientDTO)
                        .toList());
        return clientDTOResponse;
    }

    @Transactional(readOnly = true)
    public ClientDTOResponse findById(int id) {
        clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse
                .setResponse(Collections
                        .singletonList(modelMapperUtil
                                .convertClientToClientDTO(clientRepository
                                        .findById(id)
                                        .orElse(new Client()))));
        return clientDTOResponse;
    }

    public void save(ClientDTO clientDTO) {
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        client.setCreatedAt(new Date());
        clientRepository.save(client);
    }


    public void update(ClientDTO clientDTO) {
        Client clientFromDB = clientRepository.findById(clientDTO.getId()).orElse(null);
        if (clientFromDB == null) {
            save(clientDTO);
            return;
        }
        clientDTO.setCreatedAt(clientFromDB.getCreatedAt());
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        client.setUpdatedAt(new Date());
        clientRepository.save(client);
    }

    public void delete(int id) {
        if (clientRepository.findById(id).isEmpty()) {
            return;
        }
        clientRepository.deleteById(id);
    }
}
