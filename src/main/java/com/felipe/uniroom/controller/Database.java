package com.felipe.uniroom.controller;

import java.util.List;

import com.felipe.uniroom.entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Transactional
public class Database {

    public <T> List<T> findAll(Class<T> entity) {
        EntityManager em = null;

        try {
            em = ConnectionManager.getEntityManager();

            return em.createQuery("SELECT e FROM " + entity.getSimpleName() + " e", entity).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> T findById(Class<T> entity, Integer id) {
        EntityManager em = null;

        try {
            em = ConnectionManager.getEntityManager();

            return em.find(entity, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public <T> boolean saveOrUpdate(T entity) {
        EntityManager em = null;

        try {
            em = ConnectionManager.getEntityManager();

            em.persist(entity);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public <T> boolean delete(Class<T> entity, Integer id) {
        EntityManager em = null;

        try {
            em = ConnectionManager.getEntityManager();

            final T obj = em.find(entity, id);

            em.remove(obj);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public User findByUsername(String username) {
        EntityManager em = null;

        final String query = "SELECT u.password FROM User u WHERE u.username = :username";

        try {
            em = ConnectionManager.getEntityManager();

            return em.createQuery(query, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
