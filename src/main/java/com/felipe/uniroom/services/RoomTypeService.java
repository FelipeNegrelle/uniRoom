package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.RoomType;
import com.felipe.uniroom.repositories.RoomTypeRepository;
import com.felipe.uniroom.view.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RoomTypeService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private static String validateRoomType(RoomType roomType, boolean isUpdate) {
        StringBuilder errors = new StringBuilder();

        if (!isUpdate && Objects.nonNull(RoomTypeRepository.hasDuplicateName(roomType.getName(), roomType.getBranch().getIdBranch()))) {
            errors.append("Tipo já cadastrado!\n");
        }

        if (roomType.getName().isBlank()) {
            errors.append("O nome do tipo de quarto não pode ser vazio!\n");
        }

        if (roomType.getPrice() == null || roomType.getPrice() <= 0) {
            errors.append("O preço deve ser maior que zero.\n");
        }

        if (roomType.getCapacity() == null || roomType.getCapacity() <= 0) {
            errors.append("A capacidade deve ser maior que zero.\n");
        }

        if (roomType.getBranch() == null) {
            errors.append("Uma filial deve ser selecionada.\n");
        }

        Set<ConstraintViolation<RoomType>> violations = validator.validate(roomType);
        violations.forEach(violation -> errors.append(violation.getMessage()).append("\n"));

        return errors.toString();
    }

    public static boolean save(RoomType roomType) {
        try {
            String validations = validateRoomType(roomType, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return RoomTypeRepository.saveOrUpdate(roomType);
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static boolean update(RoomType roomType) {
        try {
            String validations = validateRoomType(roomType, true);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return RoomTypeRepository.saveOrUpdate(roomType);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o tipo de quarto: " + e.getMessage());
            return false;
        }
    }

    public static Boolean delete(RoomType roomType) {
        try {
            RoomType result = RoomTypeRepository.findById(RoomType.class, roomType.getIdRoomType());
            if (Objects.nonNull(result)) {
                return RoomTypeRepository.delete(RoomType.class, result.getIdRoomType());
            } else {
                JOptionPane.showMessageDialog(null, "Tipo não encontrado!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<RoomType> search(String search, String field) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }
        return RoomTypeRepository.search(RoomType.class, search, field);
    }
}