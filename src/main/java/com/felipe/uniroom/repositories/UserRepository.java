package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UserRepository extends DatabaseRepository {
    public static User findByUsername(String username) {
        final String query = "SELECT u FROM User u WHERE u.username = :username";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, User.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<User> searchByUsernameOrName(String searchText) {
        final String query = "SELECT u FROM User u WHERE u.username LIKE :searchText OR u.name LIKE :searchText";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, User.class).setParameter("searchText", "%" + searchText + "%").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Corporate> getCorporateUser(User user) {
        final String query = "SELECT c FROM Corporate c WHERE c.user.idUser = :idUser";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Corporate.class).setParameter("idUser", user.getIdUser()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static List<Branch> getBranchUser(User user) {
        final String query = "SELECT b FROM Branch b WHERE b.user.idUser = :idUser";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Branch.class).setParameter("idUser", user.getIdUser()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
