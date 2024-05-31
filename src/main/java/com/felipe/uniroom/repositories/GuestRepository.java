package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Guest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class GuestRepository extends DatabaseRepository {
    public static Guest findByCpf(String cpf) {
        final String query = "SELECT g FROM Guest g WHERE g.cpf = :cpf";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (
                NoResultException e) {
            return null;
        }
    }

    public static Boolean hasDuplicateCpf(String cpf, Integer idGuest) {
        final String query = "SELECT g FROM Guest g WHERE g.cpf = :cpf and g.idGuest <> :idGuest";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Branch.class)
                    .setParameter("cpf", cpf)
                    .setParameter("idGuest", idGuest)
                    .getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }
}
