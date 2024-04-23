package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Branch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class BranchRepository extends DatabaseRepository {
    public static Branch findByCnpj(String cnpj) {
        final String query = "SELECT b FROM Branch b WHERE b.cnpj = :cnpj";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Branch.class)
                    .setParameter("cnpj", cnpj)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Boolean hasDuplicateCnpj(String cnpj, Integer idBranch) {
        final String query = "SELECT b FROM Branch b WHERE b.cnpj = :cnpj and b.idBranch <> :idBranch";

        try(EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Branch.class)
                    .setParameter("cnpj", cnpj)
                    .setParameter("idBranch", idBranch)
                    .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}
