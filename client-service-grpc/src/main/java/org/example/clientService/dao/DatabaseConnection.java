package org.example.clientService.dao;

import org.example.clientService.models.Bank;
import org.example.clientService.models.Client;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final SessionFactory sessionFactory;

    private DatabaseConnection() {
        Configuration configuration = new Configuration().addAnnotatedClass(Bank.class).addAnnotatedClass(Client.class);
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

    public static DatabaseConnection getInstance() {
        synchronized (DatabaseConnection.class) {
            if (instance == null) {
                instance = new DatabaseConnection();
            }
        }
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
