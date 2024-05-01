package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
        EntityManager em = ConnectionManager.getEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            final T entityToDelete = em.find(entity, id);

            em.remove(entityToDelete);

            transaction.commit();

            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();

            return false;
        }
    }

    public static <T> List<T> search(Class<T> entity, String search, String field) {
        // se esse cara soltar null pointer verificar se o field foi setado
        try {
            entity.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<T> cq = cb.createQuery(entity);
            final Root<T> root = cq.from(entity);

            cq.where(cb.like(root.get(field), "%" + search + "%"));

            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
