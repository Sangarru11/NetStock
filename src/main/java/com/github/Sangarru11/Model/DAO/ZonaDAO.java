package com.github.Sangarru11.Model.DAO;

import com.github.Sangarru11.Model.Entity.Usuario;
import com.github.Sangarru11.Model.Entity.Zona;
import org.hibernate.Session;

import java.io.IOException;
import java.util.List;

public class ZonaDAO implements DAO <Zona, Integer>{

    @Override
    public boolean save(Zona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.save(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean insert(Zona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.persist(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean update(Zona entity) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        ss.update(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;
    }

    @Override
    public boolean delete(Zona entity) {
        Session ss =sF.openSession();
        ss.beginTransaction();
        ss.delete(entity);
        ss.getTransaction().commit();
        ss.close();
        return true;  }

    @Override
    public Zona findById(Integer id) {
        Session ss = sF.openSession();
        ss.beginTransaction();
        Zona zona = ss.get(Zona.class, id);
        ss.getTransaction().commit();
        ss.close();
        return zona;
    }

    @Override
    public Usuario findByName(String key) {
        return null;
    }

    @Override
    public List<Zona> findAll() {
        Session ss = sF.openSession();
        ss.beginTransaction();
        List<Zona> zonas = ss.createQuery("from Zona a join fetch a.idZona", Zona.class).list();
        ss.getTransaction().commit();
        ss.close();
        return zonas;
    }
    @Override
    public void close() throws IOException {
        sF.close();
    }
    public static ZonaDAO build(){
        return new ZonaDAO();
    }
}
