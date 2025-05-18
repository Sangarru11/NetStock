package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Movimiento;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MovimientoDAO implements DAO<Movimiento, Integer> {
    @Override
    public boolean save(Movimiento entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Movimiento entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Movimiento entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Movimiento entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Movimiento findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Movimiento movimiento = ss.get(Movimiento.class, id);
        ss.getTransaction().commit();
        ss.close();
        return movimiento;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Movimiento> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Movimiento> movimientos = ss.createQuery("from Movimiento a join fetch a.idMovimiento", Movimiento.class).list();
        ss.getTransaction().commit();
        ss.close();
        return movimientos;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }

    public static MovimientoDAO build() {
        return new MovimientoDAO();
    }
}
