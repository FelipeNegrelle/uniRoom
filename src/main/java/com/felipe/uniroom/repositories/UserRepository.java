package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserRepository extends DatabaseRepository {
    public User findByUsername(String username) {
        final String query = "SELECT u.password FROM User u WHERE u.username = :username";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
