package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.Item;
import jakarta.persistence.EntityManager;

import java.util.List;


public class InventoryRepository extends DatabaseRepository {
    public static List<Item> getItemsFromInventory(Inventory inventory) {
        final String query = "SELECT i FROM Item i JOIN InventoryItem ii ON i.idItem = ii.id.idItem WHERE ii.id.idInvetory = :idInventory";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Item.class)
                    .setParameter("idInventory", inventory.getIdInventory())
                    .getResultList();
        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
