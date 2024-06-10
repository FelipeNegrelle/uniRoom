package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Objects;

public class ReservationRepository extends DatabaseRepository {
    public static Boolean cancel(Reservation reservation) {
        try {
            final Reservation result = findById(Reservation.class, reservation.getIdReservation());

            if (result != null) {
                result.setStatus("C");

                return saveOrUpdate(result);
            } else {
                return false;
            }
        } catch (
                NoResultException e) {
            return null;
        }
    }

    public static List<Guest> getGuests(Reservation reservation) {
        try {
            final Reservation result = findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                final String query = "SELECT g FROM Guest g WHERE g.room.idRoom = :idRoom AND g.branch.idBranch = :idBranch";

                try (EntityManager em = ConnectionManager.getEntityManager()) {
                    return em.createQuery(query, Guest.class)
                            .setParameter("idRoom", reservation.getRoom().getIdRoom())
                            .setParameter("idBranch", reservation.getBranch().getIdBranch())
                            .getResultList();
                } catch (
                        NoResultException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (
                Exception e) {
            return null;
        }
    }
}
