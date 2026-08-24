package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.RoomType;
import com.felipe.uniroom.repositories.ExpenseRepository;
import com.felipe.uniroom.repositories.RoomTypeRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class RoomTypeService {
    private static String validateRoomType(RoomType roomType, boolean isUpdate) {
        StringBuilder errors = new StringBuilder();

        if (isUpdate) {
            if (RoomTypeRepository.hasDuplicateName(roomType.getName(), roomType.getIdRoomType(), roomType.getBranch().getIdBranch())) {
                errors.append("Tipo já cadastrado!\n");
            }
        } else {
            if (Objects.nonNull(RoomTypeRepository.findByName(roomType.getName(), roomType.getBranch().getIdBranch()))) {
                errors.append("Tipo já cadastrado!\n");
            }
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
            errors.append("Um estabelecimento deve ser selecionada.\n");
        }

        return errors.toString();
    }

    public static List<RoomType> findAll(Role role){
        return RoomTypeRepository.findAll(RoomType.class, role);
    }

    public static RoomType findById(int id){
        return RoomTypeRepository.findById(RoomType.class,id);
    }

    public static boolean save(RoomType roomType) {
        try {
            String validations = validateRoomType(roomType, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return RoomTypeRepository.saveOrUpdate(roomType);
        } catch (
                Exception e) {
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
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o tipo de quarto: " + e.getMessage());
            return false;
        }
    }

    public static Boolean delete(RoomType roomType) {
        try {
            final RoomType result = RoomTypeRepository.findById(RoomType.class, roomType.getIdRoomType());

            if (Objects.nonNull(result)) {
                result.setActive(false);

                return update(result);
            } else {
                JOptionPane.showMessageDialog(null, "Tipo não encontrado!");
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<RoomType> findRoomTypesByBranch(Integer branchId){
        try {
            return RoomTypeRepository.findRoomTypesByBranch(branchId);
        } catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    public static List<RoomType> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }
        return RoomTypeRepository.search(RoomType.class, search, field, role);
    }
}
