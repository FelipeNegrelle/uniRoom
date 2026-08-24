package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.RoomType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class RoomTypeRepository extends DatabaseRepository {
    public static RoomType findByName(String name, Integer idBranch) {
        final String query = "SELECT rt FROM RoomType rt WHERE rt.name = :name AND rt.branch.id = :idBranch";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, RoomType.class)
                    .setParameter("name", name)
                    .setParameter("idBranch", idBranch)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static Boolean hasDuplicateName(String name, Integer idRoomType, Integer idBranch) {
        final String query = "SELECT count(rt) FROM RoomType rt WHERE rt.name = :name AND rt.id <> :idRoomType AND rt.branch.id = :idBranch";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            Long count = em.createQuery(query, Long.class)
                    .setParameter("name", name)
                    .setParameter("idRoomType", idRoomType)
                    .setParameter("idBranch", idBranch)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static List<RoomType> findRoomTypesByBranch(Integer branchId){
        final String query = "SELECT rt from RoomType rt WHERE rt.branch.idBranch = :idBranch";

        try(EntityManager em = ConnectionManager.getEntityManager()){
            return em.createQuery(query, RoomType.class)
                    .setParameter("idBranch", branchId)
                    .getResultList();
        } catch (NoResultException e){
            return null;
        }
    }
}
