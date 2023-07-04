package org.example.clientService.dao;

import org.example.clientService.models.Bank;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankDAO {
    private static BankDAO instance;
    private final SessionFactory sessionFactory;

    private BankDAO() {
        sessionFactory = DatabaseConnection.getInstance().getSessionFactory();
    }

    public static synchronized BankDAO getInstance() {
        if (instance == null) {
            instance = new BankDAO();
        }
        return instance;
    }
    public List<Bank> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            List<Bank> banks = new ArrayList<>();
            try {
                transaction = session.beginTransaction();
                banks = session.createQuery("from Bank", Bank.class).getResultList();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return banks;
        }
    }

    public Bank findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Bank bank = new Bank();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bank = session.get(Bank.class, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return bank;
        }
    }

    public Bank findByCheckingAccount(String checkingAccount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Query<Bank> query = session.createQuery("from Bank b where b.checkingAccount = :checkingAccount", Bank.class);
                query.setParameter("checkingAccount", checkingAccount);
                Bank bank = query.uniqueResult();
                transaction.commit();
                return bank;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean save(Bank bank) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bank.setCreatedAt(new Date());
                session.save(bank);
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

    public boolean update(Bank bank) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                bank.setUpdatedAt(new Date());
                session.update(bank);
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
                session.createQuery("delete Bank b where b.id=:id")
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

