package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.MovimientoProducto;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MovimientoProductoDAO implements DAO<MovimientoProducto, Integer> {

    @Override
    public boolean save(MovimientoProducto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(MovimientoProducto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(MovimientoProducto entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(MovimientoProducto entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public MovimientoProducto findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        MovimientoProducto movimientoProducto = ss.get(MovimientoProducto.class, id);
        ss.getTransaction().commit();
        ss.close();
        return movimientoProducto;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<MovimientoProducto> findAll() {
        Session session = sF.openSession();
        session.beginTransaction();

        List<MovimientoProducto> movimientosProductos = session.createQuery("from MovimientoProducto", MovimientoProducto.class).list();

        session.getTransaction().commit();
        session.close();

        return movimientosProductos;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }
    public static MovimientoProductoDAO build() {
        return new MovimientoProductoDAO();
    }
}
