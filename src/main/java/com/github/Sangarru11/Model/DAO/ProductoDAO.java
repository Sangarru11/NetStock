package com.github.Sangarru11.Model.DAO;

    import com.github.Sangarru11.Model.Entity.Producto;
    import com.github.Sangarru11.Model.Entity.Usuario;
    import org.hibernate.Session;
    import org.hibernate.Transaction; // Asegúrate de importar org.hibernate.Transaction

    import java.io.IOException;
    import java.sql.SQLException; // No se usa directamente aquí, pero mantenido por la interfaz
    import java.util.List;

    public class ProductoDAO implements DAO<Producto, Integer> {
        @Override
        public boolean save(Producto entity) {
            Session session = null;
            Transaction tx = null;
            try {
                session = sF.openSession();
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace(); // Considera un logging más robusto
                return false;
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }

        @Override
        public boolean insert(Producto entity) {
            Session session = null;
            Transaction tx = null;
            try {
                session = sF.openSession();
                tx = session.beginTransaction();
                session.persist(entity); // persist es adecuado para entidades nuevas
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                return false;
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }

        @Override
        public boolean update(Producto entity) {
            Session session = null;
            Transaction tx = null;
            try {
                session = sF.openSession();
                tx = session.beginTransaction();
                session.merge(entity); // Usar merge para entidades que pueden estar detached
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                return false;
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }

        @Override
        public boolean delete(Producto entity) throws SQLException { // SQLException se mantiene por la interfaz, aunque Hibernate usa RuntimeExceptions
            Session session = null;
            Transaction tx = null;
            try {
                session = sF.openSession();
                tx = session.beginTransaction();
                session.delete(entity);
                tx.commit();
                return true;
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                // Si necesitas lanzar SQLException específicamente, tendrías que envolver la excepción de Hibernate.
                // throw new SQLException("Error deleting entity", e);
                return false;
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }

        @Override
        public Producto findById(Integer id) {
            Session session = null;
            Transaction tx = null; // No estrictamente necesario para una lectura simple si no hay modificaciones
            Producto producto = null;
            try {
                session = sF.openSession();
                // tx = session.beginTransaction(); // No es necesario para session.get() si autocommit está habilitado o no se modifica nada
                producto = session.get(Producto.class, id);
                // if (tx != null) tx.commit(); // Solo si se inició una transacción
            } catch (Exception e) {
                // if (tx != null && tx.isActive()) tx.rollback(); // Solo si se inició una transacción
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return producto;
        }

        @Override
        public Usuario findByName(String key) {
            // Implementación pendiente si es necesaria para ProductoDAO
            return null;
        }

        @Override
        public List<Producto> findAll() {
            Session session = null;
            // Transaction tx = null; // No estrictamente necesario para una consulta de solo lectura
            List<Producto> productos = null;
            try {
                session = sF.openSession();
                // tx = session.beginTransaction(); // No es necesario para una consulta de solo lectura
                productos = session.createQuery(
                    "select distinct p from Producto p " +
                    "left join fetch p.idCategoria " +
                    "left join fetch p.productoZonas z " +
                    "left join fetch z.idZona", Producto.class
                ).list();
                // if (tx != null) tx.commit(); // Solo si se inició una transacción
            } catch (Exception e) {
                // if (tx != null && tx.isActive()) tx.rollback(); // Solo si se inició una transacción
                e.printStackTrace();
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return productos;
        }

        @Override
        public void close() throws IOException {
            if (sF != null && !sF.isClosed()) { // sF es la SessionFactory, su cierre es global
                sF.close();
            }
        }

        public static ProductoDAO build(){
            return new ProductoDAO();
        }
    }