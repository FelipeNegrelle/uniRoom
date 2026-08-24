package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.InventoryRepository;
import com.felipe.uniroom.repositories.ItemRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class ItemService {
    private static String validateItem(Item item, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (isUpdate) {
            List<Item> existingItems = ItemRepository.findItemsByNameBranchAndNotId(item.getName(), item.getBranch().getIdBranch(), item.getIdItem());
            if (!existingItems.isEmpty()) {
                errorsSb.append("Item já cadastrado!\n");
            }
        } else {
            Item foundItem = ItemRepository.findByNameAndBranch(item.getName(), item.getBranch().getIdBranch());
            if (foundItem != null) {
                errorsSb.append("Item já cadastrado!\n");
            }
        }

        if (item.getName().isBlank()) {
            errorsSb.append("Nome do item não pode ser vazio.\n");
        }

        if (item.getPrice() == null || item.getPrice() <= 0) {
            errorsSb.append("Preço do item deve ser maior que zero.\n");
        }

        if (item.getBranch() == null) {
            errorsSb.append("O item deve estar associado a um estabelecimento.\n");
        }

        return errorsSb.toString();
    }

    public static List<Item> findAll(Role role){
        return ItemRepository.findAll(Item.class, role);
    }

    public static Item findById(int id){
        return ItemRepository.findById(Item.class,id);
    }

    public static boolean save(Item item) {
        try {
            String validations = validateItem(item, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ItemRepository.saveOrUpdate(item);
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static boolean update(Item item) {
        try {
            String validations = validateItem(item, true);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ItemRepository.saveOrUpdate(item);
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o item: " + e.getMessage());
            return false;
        }
    }

    public static Boolean delete(Item item) {
        try {
            Item result = ItemRepository.findById(Item.class, item.getIdItem());
            if (Objects.nonNull(result)) {
                return ItemRepository.delete(Item.class, result.getIdItem());
            } else {
                JOptionPane.showMessageDialog(null, "Item não encontrado!");
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<Item> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }

        return InventoryRepository.search(Item.class, search, field, role);
    }
}
