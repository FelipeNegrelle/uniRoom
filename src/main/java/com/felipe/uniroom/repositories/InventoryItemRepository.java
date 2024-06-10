package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.InventoryItem;
import com.felipe.uniroom.entities.InventoryItemId;
import jakarta.persistence.EntityManager;

public class InventoryItemRepository extends DatabaseRepository {
    public static InventoryItem findById(InventoryItemId id) {
        final String query = "SELECT ii FROM InventoryItem ii WHERE ii.id.idItem = :idItem AND ii.id.idInvetory = :idInventory";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, InventoryItem.class)
                    .setParameter("idItem", id.getIdItem())
                    .setParameter("idInventory", id.getIdInvetory())
                    .getSingleResult();
        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean delete(InventoryItem inventoryItem) {
        final String query = "DELETE FROM InventoryItem ii WHERE ii.id.idItem = :idItem AND ii.id.idInvetory = :idInventory";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            em.getTransaction().begin();
            final int deleted = em.createQuery(query)
                    .setParameter("idItem", inventoryItem.getId().getIdItem())
                    .setParameter("idInventory", inventoryItem.getId().getIdInvetory())
                    .executeUpdate();
            em.getTransaction().commit();
            return deleted > 0;
        } catch (
                Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
