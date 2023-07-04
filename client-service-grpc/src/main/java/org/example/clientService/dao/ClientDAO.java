package org.example.clientService.dao;

import org.example.clientService.models.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientDAO {

    private static ClientDAO instance;
    private final SessionFactory sessionFactory;

    private ClientDAO() {
        sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
    }

    public static ClientDAO getInstance() {
        if (instance == null) {
            instance = new ClientDAO();
        }
        return instance;
    }


    public List<Client> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            List<Client> clients = new ArrayList<>();
            try {
                transaction = session.beginTransaction();
                clients = session.createQuery("from Client", Client.class).getResultList();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return clients;
        }
    }

    public Client findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            Client client = new Client();
            try {
                transaction = session.beginTransaction();
                client = session.get(Client.class, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return client;
        }
    }

    public boolean save(Client client) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                client.setCreatedAt(new Date());
                session.save(client);
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean update(Client client) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                client.setUpdatedAt(new Date());
                session.update(client);
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.createQuery("delete Client c where c.id=:id")
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
                return true;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }


}
