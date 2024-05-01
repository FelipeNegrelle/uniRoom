package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.repositories.ReservationRepository;
import com.felipe.uniroom.view.Components;

import javax.swing.*;
import java.util.Objects;

public class ReservationService {
    private static String validateReservation(Reservation reservation) {
        final StringBuilder errorsSb = new StringBuilder();

        if (Objects.isNull(reservation.getRoom())) {
            errorsSb.append("Quarto da reserva não pode ser vazio.n\n");
        }



        if (Objects.isNull(reservation.getUser())) {
            errorsSb.append("Usuário da reserva não pode ser vazio.n\n");
        }

        if (reservation.getDays() <= 0) {
            errorsSb.append("O período da reserva não pode ser menor ou igual a zero.\n");
        }

        return errorsSb.toString();
    }

    public static Boolean save(Reservation reservation) {
        try {
            final String validations = validateReservation(reservation);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);

                return false;
            } else {
                return ReservationRepository.saveOrUpdate(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean update(Reservation reservation) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                final String validations = validateReservation(reservation);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);

                    return false;
                } else {
                    result.setIdReservation(reservation.getIdReservation());
                    result.setUser(reservation.getUser());
                    result.setBranch(reservation.getBranch());
                    result.setRoom(reservation.getRoom());
                    result.setDays(reservation.getDays());
                    result.setStatus(reservation.getStatus());

                    return ReservationRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Reserva não encontrada.");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean cancel(Reservation reservation) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if(Objects.nonNull(reservation)) {
                return ReservationRepository.cancel(reservation.getIdReservation());
            } else {
                JOptionPane.showMessageDialog(null, "Reserva não encontrada.");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }
}
