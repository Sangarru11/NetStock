package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Categoria;
import com.github.Sangarru11.Model.Entity.Tecnico;
import com.github.Sangarru11.Model.Entity.Usuario;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

public class TecnicoDAO implements DAO<Tecnico, Integer> {

    @Override
    public boolean save(Tecnico entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Tecnico entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Tecnico entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Tecnico entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public Tecnico findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Tecnico tecnico = ss.get(Tecnico.class, id);
        ss.getTransaction().commit();
        ss.close();
        return tecnico;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Tecnico> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Tecnico> tecnicos = ss.createQuery("FROM Tecnico", Tecnico.class).list();
        ss.getTransaction().commit();
        ss.close();
        return tecnicos;
    }

    @Override
    public void close() throws IOException {
        sF.close();
    }

    public static TecnicoDAO build() {
        return new TecnicoDAO();
    }
}
