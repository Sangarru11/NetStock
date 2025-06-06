package com.github.Sangarru11.Model.DAO;

    import com.github.Sangarru11.Model.Entity.Producto;
    import com.github.Sangarru11.Model.Entity.Usuario;
    import org.hibernate.Session;
    import org.hibernate.Transaction;

    import java.io.IOException;
    import java.sql.SQLException;
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
                e.printStackTrace();
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
                session.persist(entity);
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
                session.merge(entity);
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
        public boolean delete(Producto entity) throws SQLException {
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
            Transaction tx = null;
            Producto producto = null;
            try {
                session = sF.openSession();
                producto = session.get(Producto.class, id);
            } catch (Exception e) {
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
            return null;
        }

        @Override
        public List<Producto> findAll() {
            Session session = null;
            List<Producto> productos = null;
            try {
                session = sF.openSession();
                productos = session.createQuery(
                    "select distinct p from Producto p " +
                    "left join fetch p.idCategoria " +
                    "left join fetch p.productoZonas z " +
                    "left join fetch z.idZona " +
                    "left join fetch p.idProveedor",
                    Producto.class
                ).list();
            } catch (Exception e) {
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
            if (sF != null && !sF.isClosed()) {
                sF.close();
            }
        }

        public static ProductoDAO build(){
            return new ProductoDAO();
        }
    }