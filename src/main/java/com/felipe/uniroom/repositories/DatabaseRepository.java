package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.filters.AccessFilterFactory;
import com.felipe.uniroom.config.filters.AccessFilterStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

//TODO talvez tornar esta classe do tipo <T> e fazer como no spring boot
@Transactional
public class DatabaseRepository {
    public static <T> List<T> findAll(Class<T> entity, Role role) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            final CriteriaQuery<T> query = criteriaBuilder.createQuery(entity);
            final Root<T> root = query.from(entity);
            final AccessFilterStrategy accessFilter = AccessFilterFactory.create(role);

            Predicate predicate = accessFilter.createFilter(entity, root, criteriaBuilder, role);

            if (hasActiveField(entity)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("active")));
            }

            query.where(predicate);

            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T findById(Class<T> entity, Object id) {
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

    public static <T> List<T> search(Class<T> entity, String search, String field, Role role) {
        try {
            entity.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            final CriteriaQuery<T> query = criteriaBuilder.createQuery(entity);
            final Root<T> root = query.from(entity);
            final AccessFilterStrategy accessFilter = AccessFilterFactory.create(role);
            final Predicate searchFilter = criteriaBuilder.like(root.get(field).as(String.class), "%" + search + "%");
            final Predicate accessPredicate = accessFilter.createFilter(entity, root, criteriaBuilder, role);

            query.where(criteriaBuilder.and(searchFilter, accessPredicate));

            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean hasActiveField(Class<?> entity) {
        return Arrays.stream(entity.getDeclaredFields())
                .anyMatch(field -> field.getName().equals("active"));
    }
}
