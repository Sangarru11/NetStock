package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Proveedor;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

public class ProveedorDAO implements DAO<Proveedor, Integer> {

    @Override
    public boolean save(Proveedor entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Proveedor entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Proveedor entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Proveedor entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Proveedor findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Proveedor proveedor = ss.get(Proveedor.class, id);
        ss.getTransaction().commit();
        ss.close();
        return proveedor;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Proveedor> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Proveedor> proveedores = ss.createQuery("from Proveedor", Proveedor.class).list();
        ss.getTransaction().commit();
        ss.close();
        return proveedores;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }
    public static ProveedorDAO build() {
        return new ProveedorDAO();
    }
}
