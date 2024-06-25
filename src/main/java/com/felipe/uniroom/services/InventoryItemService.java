package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.InventoryItem;
import com.felipe.uniroom.repositories.InventoryItemRepository;

import javax.swing.*;
import java.util.List;

public class InventoryItemService {
    private static String validateInventoryItem(InventoryItem inventoryItem, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

//        if (inventoryItem.getInventory().getIdInventory() == null) {
//            errorsSb.append("O item do inventário deve estar associado a um inventário!\n");
//        }
//
//        if (inventoryItem.getItem().getIdItem() == null) {
//            errorsSb.append("O item do inventário deve estar associado a um item!\n");
//        }

        if (inventoryItem.getQuantity() == null) {
            errorsSb.append("A quantidade do item do inventário não pode ser vazia!\n");
        }

        if (inventoryItem.getQuantity() < 0) {
            errorsSb.append("A quantidade do item do inventário não pode ser negativa!\n");
        }

        return errorsSb.toString();
    }

    public static List<InventoryItem> findAll(Inventory inventory, Role role){
        return InventoryItemRepository.findAllByInventory(inventory);
    }

    public static InventoryItem findById(int idInventory, int id){
        return InventoryItemRepository.findById(InventoryItem.class,id);
    }

    public static Boolean save(InventoryItem inventoryItem) {
        try {
            final String validations = validateInventoryItem(inventoryItem, false);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                return InventoryItemRepository.saveOrUpdate(inventoryItem);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o inventário-item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static Boolean update(InventoryItem inventoryItem) {
        try {
            final InventoryItem result = InventoryItemRepository.findById(inventoryItem.getId());

            if (result != null) {
                final String validations = validateInventoryItem(inventoryItem, true);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);
                    return false;
                } else {
                    result.setId(inventoryItem.getId());
                    result.setQuantity(inventoryItem.getQuantity());

                    return InventoryItemRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Inventário-item não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o inventário-item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static Boolean delete(InventoryItem inventoryItem) {
        try {
            return InventoryItemRepository.delete(inventoryItem);
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao deletar o inventário-item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static List<InventoryItem> search(String search, String field, Role role) {
        return InventoryItemRepository.search(InventoryItem.class, search, field, role);//todo implementar search
//        return Collections.emptyList();
    }
}
