package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Branch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class RoomTypeRepository extends DatabaseRepository{
    public static Boolean hasDuplicateName(String name, Integer idBranch) {
        final String query = "SELECT rt FROM RoomType rt WHERE rt.name = :name AND rt.branch.id <> :idBranch";

        try(EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Branch.class)
                    .setParameter("name", name)
                    .setParameter("idBranch", idBranch)
                    .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}
