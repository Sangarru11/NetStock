package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.MovimientoProducto;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction; // Importar Transaction

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MovimientoProductoDAO implements DAO<MovimientoProducto, Integer> {

    @Override
    public boolean save(MovimientoProducto entity) {
        Session ss = sF.openSession();
        Transaction tx = null;
        try {
            tx = ss.beginTransaction();
            ss.save(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); // O manejar el error de forma más adecuada
            return false;
        } finally {
            ss.close();
        }
    }

    @Override
    public boolean insert(MovimientoProducto entity) {
        Session ss = sF.openSession();
        Transaction tx = null;
        try {
            tx = ss.beginTransaction();
            ss.merge(entity); // Cambiado de persist(entity) a merge(entity)
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace(); // Considera un logging más robusto
            return false;
        } finally {
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
    }

    @Override
    public boolean update(MovimientoProducto entity) {
        Session ss = sF.openSession();
        Transaction tx = null;
        try {
            tx = ss.beginTransaction();
            ss.update(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            ss.close();
        }
    }

    @Override
    public boolean delete(MovimientoProducto entity) throws SQLException {
        Session ss = sF.openSession();
        Transaction tx = null;
        try {
            tx = ss.beginTransaction();
            ss.delete(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); // O convertir a SQLException si es necesario
            // throw new SQLException("Error deleting entity", e); // Ejemplo
            return false;
        } finally {
            ss.close();
        }
    }

    @Override
    public MovimientoProducto findById(Integer id) {
        Session ss = sF.openSession();
        Transaction tx = null;
        MovimientoProducto movimientoProducto = null;
        try {
            tx = ss.beginTransaction();
            movimientoProducto = ss.get(MovimientoProducto.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            ss.close();
        }
        return movimientoProducto;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<MovimientoProducto> findAll() {
        Session session = sF.openSession();
        Transaction tx = null;
        List<MovimientoProducto> movimientosProductos = null;
        try {
            tx = session.beginTransaction();
            movimientosProductos = session.createQuery("from MovimientoProducto", MovimientoProducto.class).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return movimientosProductos;
    }

    @Override
    public void close() throws IOException {
        if (sF != null && !sF.isClosed()) {
            sF.close();
        }
    }
    public static MovimientoProductoDAO build() {
        return new MovimientoProductoDAO();
    }
}