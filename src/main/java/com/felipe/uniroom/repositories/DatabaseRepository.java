package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO talvez tornar esta classe do tipo <T> e fazer como no spring boot
@Transactional
public class DatabaseRepository {
    public static <T> List<T> findAll(Class<T> entity, Role role) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final Map<String, Object> params = new HashMap<>();
            final StringBuilder queryString = new StringBuilder("SELECT e FROM ").append(entity.getSimpleName()).append(" e WHERE TRUE");

            switch (role.getRole()) {
                case 'A':
                    // Administrador tem acesso a todos os registros, então não precisa de filtro
                    break;
                case 'C':
                    if (!entity.getSimpleName().equals("Corporate")) {
                        boolean hasBranch = false;

                        for (Field f : entity.getDeclaredFields()) {
                            if (f.getName().equals("branch")) {
                                hasBranch = true;
                                break;
                            }
                        }

                        if (hasBranch) {
                            queryString.append(" AND e.branch.corporate IN :corporates");
                            params.put("corporates", role.getCorporates());
                        } else {
                            queryString.append("AND e.corporate IN :corporates");
                            params.put("corporates", role.getCorporates());
                        }
                    } else {
                        queryString.append(" AND e.idCorporate IN :idCorporates");
                        params.put("idCorporates", role.getCorporates().stream().map(Corporate::getIdCorporate).toList());
                    }
                    break;
                case 'B':
                    if (!entity.getSimpleName().equals("Branch")) {
                        queryString.append(" AND e.branch IN :branches");
                        params.put("branches", role.getBranches());
                    } else {
                        queryString.append(" AND e.idBranch IN :idBranches");
                        params.put("idBranches", role.getBranches().stream().map(Branch::getIdBranch).toList());
                    }
                    break;
                case 'E':
                    if (!entity.getSimpleName().equals("Branch")) {
                        queryString.append(" AND e.branch = :branch");
                        params.put("branch", role.getBranches().getFirst());
                    } else {
                        queryString.append(" AND e.idBranch = :idBranch");
                        params.put("idBranch", role.getBranches().getFirst());
                    }
                    break;
            }

            if (Arrays.stream(entity.getDeclaredFields()).anyMatch(f -> f.getName().equals("active"))) {
                queryString.append(" AND e.active = true");
            }

            final TypedQuery<T> query = em.createQuery(queryString.toString(), entity);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.getResultList();
        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T findById(Class<T> entity, Object id) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.find(entity, id);
        } catch (
                Exception e) {
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
        } catch (
                Exception e) {
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
        } catch (
                Exception e) {
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
        } catch (
                NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<T> cq = cb.createQuery(entity);
            final Root<T> root = cq.from(entity);

            Predicate predicate = cb.like(root.get(field).as(String.class), "%" + search + "%");

            switch (role.getRole()) {
                case 'A':
                    break;
                case 'C':
                    if (entity.getSimpleName().equals("Corporate")) {
                        predicate = cb.and(predicate, root.get("idCorporate").in(role.getCorporates().stream().map(Corporate::getIdCorporate).collect(Collectors.toList())));
                    } else {
                        boolean hasBranch = false;

                        for (Field f : entity.getDeclaredFields()) {
                            if (f.getName().equals("branch")) {
                                hasBranch = true;
                                break;
                            }
                        }

                        if (hasBranch) {
                            final Join<T, Branch> branchJoin = root.join("branch");

                            predicate = cb.and(predicate, branchJoin.get("corporate").in(role.getCorporates()));
                        } else {
                            predicate = cb.and(predicate, root.get("corporate").in(role.getCorporates()));
                        }
                    }
                    break;
                case 'B':
                case 'E':
                    if (entity.getSimpleName().equals("Branch")) {
                        predicate = cb.and(predicate, root.get("idBranch").in(role.getBranches().stream().map(Branch::getIdBranch).collect(Collectors.toList())));
                    } else {
                        predicate = cb.and(predicate, root.get("branch").in(role.getBranches()));
                    }
                    break;
            }

            cq.where(predicate);

            return em.createQuery(cq).getResultList();
        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
