package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.repositories.InventoryRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class InventoryService {
    private static String validateInventory(Inventory inventory, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (Objects.isNull(inventory.getRoom())) {
            errorsSb.append("O inventário deve estar associado a um quarto!\n");
        }

        if (Objects.isNull(inventory.getBranch())) {
            errorsSb.append("O inventário deve estar associado a uma filial!\n");
        }

        if (inventory.getDescription().isBlank()) {
            errorsSb.append("Nome do inventário não pode ser vazia!\n");
        }

        if (inventory.getDescription().length() > 255) {
            errorsSb.append("Nome do inventário deve ter no máximo 255 caracteres!\n");
        }


        return errorsSb.toString();
    }

    public static Boolean save(Inventory inventory) {
        try {
            final String validations = validateInventory(inventory, false);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                return InventoryRepository.saveOrUpdate(inventory);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o inventário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static Boolean update(Inventory inventory) {
        try {
            final Inventory result = InventoryRepository.findById(Inventory.class, inventory.getIdInventory());

            if (Objects.nonNull(result)) {

                final String validations = validateInventory(inventory, true);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);

                    return false;
                } else {
                    result.setIdInventory(inventory.getIdInventory());
                    result.setRoom(inventory.getRoom());
                    result.setBranch(inventory.getBranch());
                    result.setDescription(inventory.getDescription());
                    result.setActive(inventory.getActive());

                    return InventoryRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Inventario não encontrado.");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean delete(Inventory inventory) {
        try {
            final Inventory result = InventoryRepository.findById(Inventory.class, inventory.getIdInventory());

            if (Objects.nonNull(result)) {
                return InventoryRepository.delete(Inventory.class, result.getIdInventory());
            } else {
                JOptionPane.showMessageDialog(null, "Invetario não encontrada!");

                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static List<Inventory> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "description";
        }

        return InventoryRepository.search(Inventory.class, search, field, role);
    }
}
