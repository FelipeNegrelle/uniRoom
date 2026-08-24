package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReservationRepository extends DatabaseRepository {
    public static List<Room> getAvailableRooms(Role role, Reservation currentReservation) {
        List<Room> allRooms = findAll(Room.class, role);

        if (Objects.nonNull(allRooms) && !allRooms.isEmpty()) {
            final String query = "SELECT res.room.idRoom FROM Reservation res WHERE res.status = 'CI'";
            try (EntityManager em = ConnectionManager.getEntityManager()) {
                List<Integer> reservedRoomIds = em.createQuery(query, Integer.class).getResultList();

                if (Objects.nonNull(currentReservation) && Objects.nonNull(currentReservation.getRoom())) {
                    reservedRoomIds.remove(currentReservation.getRoom().getIdRoom());
                }

                return allRooms.stream().filter(room -> !reservedRoomIds.contains(room.getIdRoom())).collect(Collectors.toList());
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
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            TypedQuery<Reservation> query = em.createQuery("SELECT r FROM Reservation r LEFT JOIN FETCH r.guestList WHERE r.idReservation = :id", Reservation.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static boolean isRoomAvailable(Integer roomId, Date checkInDate, Date checkOutDate, Integer reservationId) {
        String query = "SELECT COUNT(r) FROM Reservation r WHERE r.room.idRoom = :roomId AND r.idReservation <> :reservationId AND r.status != 'C' AND r.status != 'CO' AND (:checkInDate < r.finalDate AND :checkOutDate > r.initialDate)";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            Long count = em.createQuery(query, Long.class).setParameter("roomId", roomId).setParameter("reservationId", reservationId != null ? reservationId : -1).setParameter("checkInDate", checkInDate).setParameter("checkOutDate", checkOutDate).getSingleResult();

            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGuestAvailable(Integer guestId, Date checkInDate, Date checkOutDate, Integer reservationId) {
        String query = "SELECT COUNT(r) FROM Reservation r JOIN r.guestList g WHERE g.idGuest = :guestId AND r.idReservation <> :reservationId AND r.status != 'C' AND r.status != 'CO' AND (:checkInDate < r.finalDate AND :checkOutDate > r.initialDate)";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            Long count = em.createQuery(query, Long.class).setParameter("guestId", guestId).setParameter("reservationId", reservationId != null ? reservationId : -1).setParameter("checkInDate", checkInDate).setParameter("checkOutDate", checkOutDate).getSingleResult();

            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Guest> getConflictingGuests(List<Guest> guests, Date checkInDate, Date checkOutDate, Integer reservationId) {
        List<Guest> conflictingGuests = new ArrayList<>();
        for (Guest guest : guests) {
            if (!isGuestAvailable(guest.getIdGuest(), checkInDate, checkOutDate, reservationId)) {
                conflictingGuests.add(guest);
            }
        }
        return conflictingGuests;
    }

    public static Double getTotalDays(Integer idReservation, Date finalDate, Role role) {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CAST(rt.price * (DATEDIFF(DATE(:date), r.initial_date) + 1) AS DOUBLE) as total_days\n");
        query.append("FROM reservation r\n");
        query.append("LEFT OUTER JOIN room rr ON r.id_room = rr.id_room\n");
        query.append("LEFT OUTER JOIN room_type rt ON rt.id_room_type = rr.id_room_type\n");
        query.append("WHERE r.id_reservation = :idReservation");

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return (Double) em.createNativeQuery(query.toString()).setParameter("date", finalDate).setParameter("idReservation", idReservation).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Double getTotalExpensesByReservationId(Integer reservationId) {
        String query = "SELECT SUM(COALESCE(i.price * e.amount, 0) + COALESCE(s.price * e.amount, 0)) " + "FROM Expense e " + "LEFT JOIN e.item i " + "LEFT JOIN e.service s " + "WHERE e.reservation.idReservation = :idReservation";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            final Double result = em.createQuery(query, Double.class).setParameter("idReservation", reservationId).getSingleResult();

            return result != null ? result : 0D;
        } catch (NoResultException e) {
            return 0D;
        }
    }

    public static List<Expense> getExpenses(Integer idReservation) {
        final String query = "SELECT e from Expense e LEFT OUTER JOIN Reservation r ON e.reservation.idReservation = r.idReservation WHERE e.reservation.idReservation = :idReservation ";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Expense.class).setParameter("idReservation", idReservation).getResultList();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
