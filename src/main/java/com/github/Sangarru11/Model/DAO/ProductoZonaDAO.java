package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.ProductoZona;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductoZonaDAO implements DAO<ProductoZona, Integer> {
    @Override
    public boolean save(ProductoZona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(ProductoZona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(ProductoZona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(ProductoZona entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public ProductoZona findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ProductoZona productoZona = ss.get(ProductoZona.class, id);
        ss.getTransaction().commit();
        ss.close();
        return productoZona;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<ProductoZona> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<ProductoZona> productoZonas = ss.createQuery("from ProductoZona", ProductoZona.class).list();
        ss.getTransaction().commit();
        ss.close();
        return productoZonas;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }
}
