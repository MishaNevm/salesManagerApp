package org.example.clientService.dao;

import org.hibernate.SessionFactory;

public class ClientDAO {

    private final SessionFactory sessionFactory;

    public ClientDAO() {
        sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
    }



}
