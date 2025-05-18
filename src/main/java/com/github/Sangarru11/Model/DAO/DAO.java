package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DAO <T, K> extends Closeable {
    SessionFactory sF = new Configuration().configure().buildSessionFactory();

    boolean save(T entity);
    boolean insert(T entity);
    boolean update (T entity);
    boolean delete (T entity) throws SQLException;

    T findById(Integer id);

    Usuario findByName(String key);

    List<T> findAll();
}
