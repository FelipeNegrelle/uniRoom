package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static List<Room> getAvailableRooms(Role role, Reservation currentReservation) {
        List<Room> allRooms = findAll(Room.class, role);

        if (Objects.nonNull(allRooms) && !allRooms.isEmpty()) {
            final String query = "SELECT res.room.idRoom FROM Reservation res WHERE res.status = 'CI'";
            try (EntityManager em = ConnectionManager.getEntityManager()) {
                List<Long> reservedRoomIds = em.createQuery(query, Long.class).getResultList();

                if (Objects.nonNull(currentReservation) && Objects.nonNull(currentReservation.getRoom())) {
                    reservedRoomIds.remove(currentReservation.getRoom().getIdRoom());
                }

                return allRooms.stream()
                        .filter(room -> !reservedRoomIds.contains(room.getIdRoom()))
                        .collect(Collectors.toList());
            } catch (NoResultException e) {
                return allRooms;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
