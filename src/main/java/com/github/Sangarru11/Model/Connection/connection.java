package com.github.Sangarru11.Model.Connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class connection {
    private static connection instance;

    // 2. Atributo para la "fábrica de sesiones"
    private SessionFactory sessionFactory;

    // 3. Constructor privado
    private connection() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al construir la SessionFactory");
        }
    }

    public static connection getInstance() {
        if (instance == null) {
            instance = new connection();
        }
        return instance;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
