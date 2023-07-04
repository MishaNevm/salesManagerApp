package org.example.orderService.dao;

import org.example.orderService.models.Order;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderDAO {

    private static OrderDAO instance;
    private final SessionFactory sessionFactory;

    private OrderDAO() {
        Configuration configuration = new Configuration().addAnnotatedClass(Order.class);
        configuration.configure("hibernate.cfg.xml");
        Flyway flyway = Flyway.configure()
                .dataSource(configuration.getProperty("hibernate.connection.url"),
                        configuration.getProperty("hibernate.connection.username"),
                        configuration.getProperty("hibernate.connection.password"))
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();

        sessionFactory = configuration.buildSessionFactory();
    }

    public static OrderDAO getInstance() {
        synchronized (OrderDAO.class) {
            if (instance == null) {
                instance = new OrderDAO();
            }
        }
        return instance;
    }

    public List<Order> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            List<Order> orders = new ArrayList<>();
            try {
                transaction = session.beginTransaction();
                orders = session.createQuery("from Order", Order.class).getResultList();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return orders;
        }
    }

    public Order findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            Order order = new Order();
            try {
                transaction = session.beginTransaction();
                order = session.get(Order.class, id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return order;
        }
    }

    public boolean save(Order order) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                order.setCreatedAt(new Date());
                session.save(order);
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

    public boolean update(Order order) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                order.setUpdatedAt(new Date());
                session.update(order);
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
                session.createQuery("delete Order o where o.id = :id")
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

