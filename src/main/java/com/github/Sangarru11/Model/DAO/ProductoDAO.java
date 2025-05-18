package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Producto;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductoDAO implements DAO<Producto, Integer> {
    @Override
    public boolean save(Producto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Producto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Producto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Producto entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Producto findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Producto producto = ss.get(Producto.class, id);
        ss.getTransaction().commit();
        ss.close();
        return producto;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Producto> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Producto> productos = ss.createQuery("from Producto a join fetch a.idProducto", Producto.class).list();
        ss.getTransaction().commit();
        ss.close();
        return productos;
    }

    @Override
    public void close() throws IOException {

    }
}
