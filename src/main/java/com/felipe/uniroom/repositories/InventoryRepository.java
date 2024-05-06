package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Inventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;


public class InventoryRepository extends DatabaseRepository{

    public static List<Inventory> findByDescriptionLike(String description) {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            TypedQuery<Inventory> query = em.createQuery("SELECT i FROM Inventory i WHERE i.description LIKE :desc", Inventory.class);
            query.setParameter("desc", "%" + description + "%");
            return query.getResultList();
        }
    }

    public static List<Inventory> findAll() {
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            TypedQuery<Inventory> query = em.createQuery("SELECT i FROM Inventory i", Inventory.class);
            return query.getResultList();
        }
    }
}
