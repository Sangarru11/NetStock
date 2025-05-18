package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UsuarioDAO implements DAO<Usuario, Integer> {
    @Override
    public boolean save(Usuario entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Usuario entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Usuario entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Usuario entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Usuario findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Usuario usuario = ss.get(Usuario.class, id);
        ss.getTransaction().commit();
        ss.close();
        return usuario;
    }
    @Override
    public Usuario findByName(String key) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Usuario usuario = ss.createQuery("from Usuario a where a.nombreUsuario = :key", Usuario.class)
                .setParameter("key", key)
                .uniqueResult();
        ss.getTransaction().commit();
        ss.close();
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Usuario> usuarios = ss.createQuery("from Usuario a join fetch a.idUsuario", Usuario.class).list();
        ss.getTransaction().commit();
        ss.close();
        return usuarios;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }

    public static UsuarioDAO build(){
        return new UsuarioDAO();
    }
}
