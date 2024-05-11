package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Collections;
import java.util.List;

public class ItemRepository extends DatabaseRepository{
    public static List<Item> findByNameAndBranch(String name, Integer idBranch) {
        final String query = "SELECT i FROM Item i WHERE i.name = :name AND i.branch.id = :idBranch";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Item.class)
                    .setParameter("name", name)
                    .setParameter("idBranch", idBranch)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public static List<Item> findItemsByNameBranchAndNotId(String name, Integer idBranch, Integer idItem) {
        final String query = "SELECT i FROM Item i WHERE i.name = :name AND i.branch.id = :idBranch AND i.idItem != :idItem";
        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Item.class)
                    .setParameter("name", name)
                    .setParameter("idBranch", idBranch)
                    .setParameter("idItem", idItem)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
