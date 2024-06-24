package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.repositories.ReservationRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReservationService {
    private static String validateReservation(Reservation reservation) {
        final StringBuilder errorsSb = new StringBuilder();
        final LocalDateTime now = LocalDateTime.now();

        if (Objects.isNull(reservation.getRoom())) {
            errorsSb.append("Quarto da reserva não pode ser vazio.\n");
        }

        if (Objects.isNull(reservation.getBranch())) {
            errorsSb.append("Filial da reserva não pode ser vazio.\n");
        }

        if (Objects.isNull(reservation.getUser())) {
            errorsSb.append("Usuário da reserva não pode ser vazio.\n");
        }

        if (Objects.isNull(reservation.getGuestList()) || reservation.getGuestList().isEmpty()) {
            errorsSb.append("A reserva deve ter pelo menos um hóspede.\n");
        }

        if (Objects.nonNull(reservation.getFinalDate()) && reservation.getFinalDate().before(reservation.getInitialDate())) {
            errorsSb.append("Data de saída deve ser após a data de entrada!\n");
        }

        LocalDateTime checkInDateTime = convertToLocalDateTime(reservation.getInitialDate());
        LocalDateTime checkOutDateTime = convertToLocalDateTime(reservation.getFinalDate());

        if (Objects.nonNull(checkInDateTime) && checkInDateTime.isBefore(now.toLocalDate().atStartOfDay())) {
            errorsSb.append("Data de entrada deve ser igual ou posterior à data atual!\n");
        }

        if (Objects.nonNull(checkOutDateTime) && checkOutDateTime.isBefore(now.toLocalDate().atStartOfDay())) {
            errorsSb.append("Data de saída deve ser igual ou posterior à data atual!\n");
        }

//        if (Objects.nonNull(reservation.getRoom()) && Objects.nonNull(reservation.getGuestList())) {
//            if (reservation.getRoom().getRoomType().getCapacity() < reservation.getGuestList().size()) {
//                errorsSb.append("A quantidade de hóspedes deve ser menor ou igual à capacidade do quarto (Capacidade: ")
//                        .append(reservation.getRoom().getRoomType().getCapacity())
//                        .append(").\n");
//            }
//        }
        //essa verificacao eh para ver se a quantidade de hospede respeita a capacidade do quarto, mas quando vou realizar o checkin ele dar erro (verificar ao finalizar projeto)

        if (!ReservationRepository.isRoomAvailable(reservation.getRoom().getIdRoom(), reservation.getInitialDate(), reservation.getFinalDate(), reservation.getIdReservation())) {
            errorsSb.append("O quarto selecionado está ocupado no período escolhido.\n");
        }


        return errorsSb.toString();
    }

    public static List<Reservation> findAll(Role role){
        return ReservationRepository.findAll(Reservation.class, role);
    }

    public static Reservation findById(int id){
        return ReservationRepository.findById(Reservation.class,id);
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
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
                    result.setStatus(reservation.getStatus());
                    result.setGuestList(reservation.getGuestList());
                    result.setInitialDate(reservation.getInitialDate());
                    result.setFinalDate(reservation.getFinalDate());
                    result.setDateTimeCheckIn(reservation.getDateTimeCheckIn());
                    result.setDateTimeCheckOut(reservation.getDateTimeCheckOut());

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

            if (Objects.nonNull(result)) {
                result.setStatus("C");

                return ReservationRepository.cancel(result);
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

    public static Boolean checkin(Reservation reservation) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                result.setStatus("CI");
                result.setDateTimeCheckIn(new Date());

                return ReservationService.update(result);
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

    public static Boolean checkout(Reservation reservation) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                result.setStatus("CO");
                result.setDateTimeCheckOut(new Date());

                return ReservationService.update(result);
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

    public static Reservation findByIdWithGuests(int idreservation){
        return ReservationRepository.findByIdWithGuests(idreservation);
    }
}
