package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Date;
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

    public static List<Room> getAvailableRooms(Role role, Reservation currentReservation) {
        List<Room> allRooms = findAll(Room.class, role);

        if (Objects.nonNull(allRooms) && !allRooms.isEmpty()) {
            final String query = "SELECT res.room.idRoom FROM Reservation res WHERE res.status = 'CI' OR res.status = 'H'";
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


    public static Reservation findByIdWithGuests(Integer id) {
        EntityManager em = ConnectionManager.getEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r LEFT JOIN FETCH r.guestList WHERE r.idReservation = :id",
                    Reservation.class
            );
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static boolean isRoomAvailable(Integer roomId, Date checkInDate, Date checkOutDate, Integer reservationId) {
        String query = "SELECT COUNT(r) FROM Reservation r WHERE r.room.idRoom = :roomId AND r.idReservation <> :reservationId AND r.status != 'C' AND r.status != 'CO' AND (:checkInDate < r.finalDate AND :checkOutDate > r.initialDate)";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            Long count = em.createQuery(query, Long.class)
                    .setParameter("roomId", roomId)
                    .setParameter("reservationId", reservationId != null ? reservationId : -1)
                    .setParameter("checkInDate", checkInDate)
                    .setParameter("checkOutDate", checkOutDate)
                    .getSingleResult();

            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
