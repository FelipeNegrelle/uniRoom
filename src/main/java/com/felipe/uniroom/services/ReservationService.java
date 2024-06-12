package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.repositories.GuestRepository;
import com.felipe.uniroom.repositories.ReservationRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        if(Objects.isNull(reservation.getInitialDate())) {
            errorsSb.append("Data inicial da reserva não pode ser vazia.\n");
        }

        if(Objects.isNull(reservation.getFinalDate())) {
            errorsSb.append("Data final da reserva não pode ser vazia.\n");
        }

        if (Objects.isNull(guests) || guests.isEmpty()) {
            errorsSb.append("A reserva deve ter pelo menos um hóspede.\n");
        }

        if(reservation.getFinalDate().toInstant().isBefore(reservation.getInitialDate().toInstant())){
            errorsSb.append("Data de saída deve ser após a data de entrada!\n");
        }

        LocalDate checkInDate = reservation.getInitialDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate checkOutDate = reservation.getFinalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final LocalDateTime now = LocalDateTime.now();
        LocalDate currentDate = now.toLocalDate();

        if (checkInDate.isBefore(currentDate) || checkOutDate.isBefore(currentDate)) {
            errorsSb.append("Datas de entrada e saída devem ser iguais ou posteriores à data atual!\n");
        }

        if(checkInDate.equals(checkOutDate)){
            errorsSb.append("Datas de entrada e saída não devem ser iguais!");
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
//                for (Guest guest : guests) {
//                    guest.setRoom(reservation.getRoom());
//                    guest.setHosted(true);
//                    GuestService.update(guest);
//                }

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
                    // Atualiza as informações da reserva
                    result.setIdReservation(reservation.getIdReservation());
                    result.setUser(reservation.getUser());
                    result.setBranch(reservation.getBranch());
                    result.setRoom(reservation.getRoom());
                    result.setStatus(reservation.getStatus());
                    result.setGuestList(reservation.getGuestList());
                    result.setFinalDate(reservation.getFinalDate());
                    result.setInitialDate(reservation.getInitialDate());

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
