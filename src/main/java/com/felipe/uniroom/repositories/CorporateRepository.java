package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Corporate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class CorporateRepository extends DatabaseRepository {
    public static Corporate findByCnpj(String cnpj) {
        final String query = "SELECT c FROM Corporate c WHERE c.cnpj = :cnpj";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Corporate.class)
                    .setParameter("cnpj", cnpj)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
