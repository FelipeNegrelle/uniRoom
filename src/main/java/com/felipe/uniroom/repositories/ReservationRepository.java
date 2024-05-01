package com.felipe.uniroom.repositories;

import com.felipe.uniroom.entities.Reservation;
import jakarta.persistence.NoResultException;

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
        } catch (NoResultException e) {
            return null;
        }
    }
}
