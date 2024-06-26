package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.entities.RoomType;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RoomService {
    private static String validateRoom(Room room, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (isUpdate) {
            List<Room> existingRooms = RoomRepository.findRoomsByNumberBranchAndNotId(room.getRoomNumber(), room.getBranch().getIdBranch(), room.getIdRoom());
            if (!existingRooms.isEmpty()) {
                errorsSb.append("Quarto com este número já cadastrado nesta filial\n");
            }
        } else {
            List<Room> existingRooms = RoomRepository.findRoomsByNumberAndBranch(room.getRoomNumber(), room.getBranch().getIdBranch());
            if (!existingRooms.isEmpty()) {
                errorsSb.append("Quarto com este número já cadastrado nesta filial\n");
            }
        }

        if (room.getRoomNumber() == null || room.getRoomNumber() <= 0) {
            errorsSb.append("Número do quarto não pode ser vazio ou zero\n");
        }

        if (room.getRoomNumber() > 9999) {
            errorsSb.append("Número do quarto não pode ser maior que 9999\n");
        }

        if (room.getBranch() == null) {
            errorsSb.append("Filial não pode ser vazia\n");
        }

        if (room.getRoomType() == null) {
            errorsSb.append("Tipo de quarto não pode ser vazio\n");
        }

        return errorsSb.toString();
    }

    public static List<Room> findAll(Role role){
        return RoomRepository.findAll(Room.class, role);
    }

    public static Room findById(int id){
        return RoomRepository.findById(Room.class,id);
    }

    public static Boolean save(Room room) {
        try {

            final String validations = validateRoom(room, false);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);

                return false;
            } else {
                return RoomRepository.saveOrUpdate(room);
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean update(Room room) {
        try {
            final Room result = RoomRepository.findById(Room.class, room.getIdRoom());

            if (Objects.nonNull(result)) {

                final String validations = validateRoom(room, true);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);

                    return false;
                } else {
                    result.setIdRoom(room.getIdRoom());
                    result.setRoomNumber(room.getRoomNumber());
                    result.setBranch(room.getBranch());
                    result.setRoomType(room.getRoomType());
                    result.setActive(room.getActive());

                    return RoomRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Matriz não encontrada.");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean delete(Room room) {
        try {
            Room result = RoomRepository.findById(Room.class, room.getIdRoom());
            if (Objects.nonNull(result)) {
                return RoomRepository.delete(Room.class, result.getIdRoom());
            } else {
                JOptionPane.showMessageDialog(null, "Quarto não encontrado!");
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<Room> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "roomNumber";
        }
        return RoomRepository.search(Room.class, search, field, role);
    }

    public static List<RoomType> findByBranchId(List<Integer> idBranches){
        return RoomRepository.findByBranchId(idBranches);
    }

    public static List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate) {
        try {
            return RoomRepository.findAvailableRooms(checkInDate, checkOutDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
