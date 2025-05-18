package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Categoria;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoriaDAO implements DAO<Categoria, Integer> {

    @Override
    public boolean save(Categoria entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Categoria entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Categoria entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Categoria entity) throws SQLException {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Categoria findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Categoria categoria = ss.get(Categoria.class, id);
        ss.getTransaction().commit();
        ss.close();
        return categoria;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Categoria> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Categoria> categorias = ss.createQuery("from Categoria a join fetch a.idCategoria", Categoria.class).list();
        ss.getTransaction().commit();
        ss.close();
        return categorias;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }
    public static CategoriaDAO build() {
        return new CategoriaDAO();
    }
}
