package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class DatabaseRepository {
    public static <T> List<T> findAll(Class<T> entity) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery("SELECT e FROM " + entity.getSimpleName() + " e", entity).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T findById(Class<T> entity, Integer id) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.find(entity, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean saveOrUpdate(T entity) {
        EntityManager em = null;

        EntityTransaction transaction = null;

        try {
            em = ConnectionManager.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.merge(entity);

            transaction.commit();

            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();
            return false;
        } finally {
            ConnectionManager.closeEntityManager(em);
        }
    }

    public static <T> boolean delete(Class<T> entity, Integer id) {
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
