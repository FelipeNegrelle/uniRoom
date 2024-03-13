package com.felipe.uniroom.controller;

import java.util.List;

import com.felipe.uniroom.entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional
public class Database {
    @PersistenceContext
    private static EntityManager em;

    public <T> List<T> findAll(Class<T> entity) {
        try {
            return em.createQuery("SELECT e FROM " + entity.getSimpleName() + " e", entity).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T findById(Class<T> entity, Integer id) {
        try {
            return em.find(entity, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> boolean saveOrUpdate(T entity) {
        try {
            em.persist(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> boolean delete(Class<T> entity, Integer id) {
        try {
            final T obj = em.find(entity, id);
            em.remove(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User findByUsername(String username) {
        final String query = "SELECT u FROM User u WHERE u.username = :username";

        try {
            return em.createQuery(query, User.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
