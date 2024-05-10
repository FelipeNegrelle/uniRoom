package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.entities.RoomType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Collections;
import java.util.List;

public class RoomRepository extends DatabaseRepository{

    public static List<RoomType> findByBranchId(Integer idBranch) {
        final String query = "SELECT rt FROM RoomType rt WHERE rt.branch.id = :idBranch";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, RoomType.class)
                    .setParameter("idBranch", idBranch)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static List<Room> findRoomsByNumberAndBranch(Integer roomNumber, Integer idBranch) {
        final String query = "SELECT r FROM Room r WHERE r.roomNumber = :roomNumber AND r.branch.id = :idBranch";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Room.class)
                    .setParameter("roomNumber", roomNumber)
                    .setParameter("idBranch", idBranch)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static List<Room> findRoomsByNumberBranchAndNotId(Integer roomNumber, Integer idBranch, Integer roomId) {
        final String query = "SELECT r FROM Room r WHERE r.roomNumber = :roomNumber AND r.branch.id = :idBranch AND r.idRoom != :roomId";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Room.class)
                    .setParameter("roomNumber", roomNumber)
                    .setParameter("idBranch", idBranch)
                    .setParameter("roomId", roomId)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
