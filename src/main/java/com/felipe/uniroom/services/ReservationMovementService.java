package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.ReservationMovement;
import com.felipe.uniroom.repositories.ReservationMovementRespository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.Objects;

public class ReservationMovementService {
    private static String validateMovement(ReservationMovement movement, Role role) {
        final StringBuilder errorsSb = new StringBuilder();

        if (Objects.isNull(movement)) {
            errorsSb.append("Movimentação não pode ser nula");
        }

        if (Objects.isNull(movement.getReservation())) {
            errorsSb.append("Reserva não pode ser nula");
        }

        return errorsSb.toString();
    }



    public static Boolean save(ReservationMovement movement, Role role) {
        try {

            final String validations = validateMovement(movement, role);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                return ReservationMovementRespository.saveOrUpdate(movement);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }
}
