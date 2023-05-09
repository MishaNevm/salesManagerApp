package com.example.clientService.dao;

import com.example.clientService.models.Client;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
@Transactional(readOnly = true)
public class ClientDAO {
    private final EntityManager entityManager;

    @Autowired
    public ClientDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Client loadClientById(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.load(Client.class, id);
    }
}
