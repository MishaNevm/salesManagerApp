package org.example.clientService.dao;

import org.hibernate.SessionFactory;

public class BankDAO {
    private final SessionFactory sessionFactory;

    public BankDAO() {
        sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
    }



}
