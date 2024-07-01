package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.repositories.GuestRepository;
import com.felipe.uniroom.views.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GuestService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private static String validateGuest(Guest guest, boolean isUpdate, Role role) {
        final StringBuilder errorsSb = new StringBuilder();

        final Set<ConstraintViolation<Guest>> violations = validator.validate(guest);

        if (!violations.isEmpty()) {
            violations.forEach(violation -> errorsSb.append(violation.getMessage().equals("número do registro de contribuinte individual brasileiro (CPF) inválido") ? "CPF Inválido!" : violation.getMessage()).append("\n"));
        }

        if (guest.getName().isBlank()) {
            errorsSb.append("Nome do hóspede não pode ser vazio!\n");
        }

        if (guest.getName().length() > 50) {
            errorsSb.append("Nome do hóspede deve ter no máximo 50 caracteres!\n");
        }

        if(guest.getIsForeigner()) {
            if (guest.getPassportNumber().length() > 9) {
                errorsSb.append("Passaporte do hóspede deve ter no máximo 9 caracteres!\n");
            }
            if (isUpdate) {
                if (GuestRepository.hasDuplicatePassport(guest.getPassportNumber(), guest.getIdGuest())) {
                    errorsSb.append("Passaporte já cadastrado!\n");
                }
            } else {
                if (Objects.nonNull(GuestRepository.findByPassport(guest.getPassportNumber()))) {
                    errorsSb.append("Passaporte já cadastrado!\n");
                }
            }
        } else {
            if (isUpdate) {
                if (GuestRepository.hasDuplicateCpf(guest.getCpf(), guest.getIdGuest(), role)) {
                    errorsSb.append("CPF já cadastrado!\n");
                }
            } else {
                if (Objects.nonNull(GuestRepository.findByCpf(guest.getCpf(), role))) {
                    errorsSb.append("CPF já cadastrado!\n");
                }
            }
        }

        return errorsSb.toString();
    }

    public static List<Guest> findAll(Role role){
        return GuestRepository.findAll(Guest.class, role);
    }

    public static Guest findById(int id){
        return GuestRepository.findById(Guest.class,id);
    }

    public static Boolean isHosted(Guest guest) {
        return GuestRepository.isHosted(guest);
    }

    public static Boolean save(Guest guest, Role role) {
        try {
            if (Objects.nonNull(guest.getCpf())) {
                guest.setCpf(guest.getCpf().replaceAll("[^0-9]", ""));
            }

            final String validations = validateGuest(guest, false, role);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                return GuestRepository.saveOrUpdate(guest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return null;
        }
    }

    public static Boolean update(Guest guest, Role role) {
        try {
            final Guest result = GuestRepository.findById(Guest.class, guest.getIdGuest());

            if (Objects.nonNull(result)) {
                if (Objects.nonNull(guest.getCpf())) {
                    guest.setCpf(guest.getCpf().replaceAll("[^0-9]", ""));
                }

                final String validations = validateGuest(guest, true, role);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);
                    return false;
                } else {
                    result.setIdGuest(guest.getIdGuest());
                    result.setName(guest.getName());
                    result.setCpf(guest.getCpf());
                    result.setIsForeigner(guest.getIsForeigner());
                    result.setPassportNumber(guest.getPassportNumber());

                    return GuestRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Hóspede não encontrado!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static Boolean delete(Guest guest) {
        try {
            final Guest result = GuestRepository.findById(Guest.class, guest.getIdGuest());

            if (Objects.nonNull(result)) {
                return GuestRepository.delete(Guest.class, result.getIdGuest());
            } else {
                JOptionPane.showMessageDialog(null, "Hóspede não encontrado!");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static List<Guest> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }

        return GuestRepository.search(Guest.class, search, field, role);
    }
}

