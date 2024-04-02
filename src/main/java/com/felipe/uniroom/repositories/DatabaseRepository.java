package com.felipe.uniroom.repositories;

import java.util.List;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Transactional
public class DatabaseRepository {

    public <T> List<T> findAll(Class<T> entity) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery("SELECT e FROM " + entity.getSimpleName() + " e", entity).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T findById(Class<T> entity, Integer id) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.find(entity, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> boolean saveOrUpdate(T entity) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            em.persist(entity);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> boolean delete(Class<T> entity, Integer id) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final T obj = em.find(entity, id);

            em.remove(obj);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
