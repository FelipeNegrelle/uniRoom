package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.repositories.ReservationRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReservationService {
    private static String validateReservation(Reservation reservation, Character action) {
        final StringBuilder errorsSb = new StringBuilder();

        final LocalDateTime now = LocalDate.now().atStartOfDay();
        if (action.equals('S')) {
            if (Objects.isNull(reservation.getRoom())) {
                errorsSb.append("Quarto da reserva não pode ser vazio.\n");
            }

            if (Objects.isNull(reservation.getBranch())) {
                errorsSb.append("Estabelecimento da reserva não pode ser vazio.\n");
            }

            if (Objects.isNull(reservation.getUser())) {
                errorsSb.append("Usuário da reserva não pode ser vazio.\n");
            }

            if (Objects.isNull(reservation.getGuestList()) || reservation.getGuestList().isEmpty()) {
                errorsSb.append("A reserva deve ter pelo menos um hóspede.\n");
            }

            if (Objects.nonNull(reservation.getInitialDate()) && reservation.getInitialDate().before(new Date())) {
                errorsSb.append("Data de entrada deve ser igual ou após a data de hoje!\n");
            }

            if (Objects.nonNull(reservation.getFinalDate()) && reservation.getFinalDate().before(reservation.getInitialDate())) {
                errorsSb.append("Data de saída deve ser após a data de entrada!\n");
            }

            if (reservation.getFinalDate().before(new Date())) {
                errorsSb.append("Data de saída deve ser igual ou após a data de hoje!\n");
            }

            if (Objects.nonNull(reservation.getRoom()) && Objects.nonNull(reservation.getGuestList())) {
                if (reservation.getRoom().getRoomType().getCapacity() < reservation.getGuestList().size()) {
                    errorsSb.append("A quantidade de hóspedes deve ser menor ou igual à capacidade do quarto (Capacidade: ");
                    errorsSb.append(reservation.getRoom().getRoomType().getCapacity());
                    errorsSb.append(").\n");
                }
            }

            if (!ReservationRepository.isRoomAvailable(reservation.getRoom().getIdRoom(), reservation.getInitialDate(), reservation.getFinalDate(), reservation.getIdReservation())) {
                errorsSb.append("O quarto selecionado está ocupado no período escolhido.\n");
            }

            final List<Guest> conflictingGuests = ReservationRepository.getConflictingGuests(reservation.getGuestList(), reservation.getInitialDate(), reservation.getFinalDate(), reservation.getIdReservation());

            if (!conflictingGuests.isEmpty()) {
                for (Guest guest : conflictingGuests) {
                    errorsSb.append("O hóspede ").append(guest.getName()).append(" já possui uma reserva no período escolhido.\n");
                }
            }
        }

        if (action.equals('I')) {
            if (Objects.nonNull(reservation.getDateTimeCheckIn())) {
                final LocalDateTime checkInDateTime = reservation.getDateTimeCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();

                System.out.println(checkInDateTime);
                System.out.println(now);
                System.out.println(reservation.getFinalDate());

                if (checkInDateTime.isBefore(now)) {
                    errorsSb.append("Data de entrada deve ser igual à data atual!\n");
                }
            } else {
                errorsSb.append("Data de entrada deve ser preenchida!\n");
            }
        }

        if (action.equals('O')) {
            if (Objects.nonNull(reservation.getDateTimeCheckOut())) {
                final LocalDateTime checkOutDateTime = convertToLocalDateTime(reservation.getDateTimeCheckOut());

                if (checkOutDateTime.isBefore(now)) {
                    errorsSb.append("Data de saída deve ser igual à data atual!\n");
                }
            } else {
                errorsSb.append("Data de saída deve ser preenchida!\n");
            }
        }

        return errorsSb.toString();
    }

    public static List<Reservation> findAll(Role role) {
        return ReservationRepository.findAll(Reservation.class, role);
    }

    public static Reservation findById(int id) {
        return ReservationRepository.findById(Reservation.class, id);
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Boolean save(Reservation reservation) {
        try {
            final String validations = validateReservation(reservation, 'S');

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

    public static Boolean update(Reservation reservation, Character action) {
        try {
            final Reservation result = ReservationRepository.findById(Reservation.class, reservation.getIdReservation());

            if (Objects.nonNull(result)) {
                final String validations = validateReservation(reservation, Objects.nonNull(action) ? action : 'U');

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

                return update(result, 'C');
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

                System.out.println(result);

                return ReservationService.update(result, 'I');
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

                return ReservationService.update(result, 'O');
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

    public static Double getTotalDays(Reservation reservation, Date checkoutDate, Role role) {
        try {
            return ReservationRepository.getTotalDays(reservation.getIdReservation(), checkoutDate, role);
        } catch (Exception e) {
            e.printStackTrace();

            return 0D;
        }
    }

    public static Double getTotalExpensesReservation(Integer reservationId) {
        try {
            return ReservationRepository.getTotalExpensesByReservationId(reservationId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0D;
        }
    }

    public static List<Expense> getExpensesReservation(Reservation reservation, Role role) {
        try {
            return ReservationRepository.getExpenses(reservation.getIdReservation());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Reservation findByIdWithGuests(int idReservation) {
        return ReservationRepository.findByIdWithGuests(idReservation);
    }
}
