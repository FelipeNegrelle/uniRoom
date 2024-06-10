package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.repositories.ReservationRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class ReservationService {
    private static String validateReservation(Reservation reservation, List<Guest> guests) {
        final StringBuilder errorsSb = new StringBuilder();

        if (Objects.isNull(reservation.getRoom())) {
            errorsSb.append("Quarto da reserva não pode ser vazio.\n");
        }

        if (Objects.isNull(reservation.getBranch())) {
            errorsSb.append("Filial da reserva não pode ser vazio.\n");
        }

        if (Objects.isNull(reservation.getUser())) {
            errorsSb.append("Usuário da reserva não pode ser vazio.\n");
        }

        if (reservation.getDays() <= 0) {
            errorsSb.append("O período da reserva não pode ser menor ou igual a zero.\n");
        }

        if (Objects.isNull(guests) || guests.isEmpty()) {
            errorsSb.append("A reserva deve ter pelo menos um hóspede.\n");
        }

        return errorsSb.toString();
    }

    public static Boolean save(Reservation reservation, List<Guest> guests) {
        try {
            final String validations = validateReservation(reservation, guests);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);

                return false;
            } else {
                for (Guest guest : guests) {
                    guest.setRoom(reservation.getRoom());
                    guest.setHosted(true);
                    GuestService.update(guest);
                }

                return ReservationRepository.saveOrUpdate(reservation);
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean update(Reservation reservation, List<Guest> guests) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                final String validations = validateReservation(reservation, guests);

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

                    for (Guest guest : guests) {
                        guest.setRoom(reservation.getRoom());
                        guest.setHosted(true);
                        GuestService.update(guest);
                    }

                    return ReservationRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Reserva não encontrada.");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean cancel(Reservation reservation, List<Guest> guests) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                result.setStatus("C");

                for(Guest guest : guests) {
                    guest.setRoom(null);
                    guest.setHosted(false);
                    GuestService.update(guest);
                }

                return ReservationRepository.cancel(result);
            } else {
                JOptionPane.showMessageDialog(null, "Reserva não encontrada.");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }
}
