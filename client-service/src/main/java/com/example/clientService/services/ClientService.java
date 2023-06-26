package com.example.clientService.services;


import com.example.clientService.dto.ClientDTO;
import com.example.clientService.dto.ClientDTOResponse;
import com.example.clientService.models.Client;
import com.example.clientService.repositoryes.ClientRepository;
import com.example.clientService.util.ModelMapperUtil;
import com.example.clientService.util.clientUtil.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@Transactional(readOnly = true)
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

    public ClientDTOResponse findAll() {
        clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse
                .setResponse(clientRepository.findAll()
                        .stream()
                        .map(modelMapperUtil::convertClientToClientDTO)
                        .toList());
        return clientDTOResponse;
    }

    public ClientDTOResponse findById(int id) {
        clientDTOResponse = new ClientDTOResponse();
        clientDTOResponse
                .setResponse(Collections
                        .singletonList(modelMapperUtil
                                .convertClientToClientDTO(clientRepository
                                        .findById(id)
                                        .orElseThrow(ClientNotFoundException::new))));
        return clientDTOResponse;
    }

    @Transactional
    public void save(ClientDTO clientDTO) {
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        client.setCreatedAt(new Date());
        clientRepository.save(client);
    }


    @Transactional
    public void update(ClientDTO clientDTO) {
        clientDTO.setCreatedAt(findById(clientDTO.getId()).getResponse().get(0).getCreatedAt());
        Client client = modelMapper.convertClientDTOToClient(clientDTO);
        client.setUpdatedAt(new Date());
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        clientRepository.deleteById(id);
    }

}
