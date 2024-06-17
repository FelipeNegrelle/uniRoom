package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.Expense;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class ExpenseRepository extends DatabaseRepository {
    public static boolean hasDuplicateItem(Expense expense) {
        final String query = "SELECT e.idExpense FROM Expense e WHERE e.reservation.idReservation = :idReservation AND e.item.idItem = :idItem";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Expense.class)
                    .setParameter("idReservation", expense.getReservation().getIdReservation())
                    .setParameter("idItem", expense.getItem().getIdItem())
                    .getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }

    public static boolean hasDuplicateService(Expense expense) {
        final String query = "SELECT e.idExpense FROM Expense e WHERE e.reservation.idReservation = :idReservation AND e.service.idService = :idService";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Expense.class)
                    .setParameter("idReservation", expense.getReservation().getIdReservation())
                    .setParameter("idService", expense.getService().getIdService())
                    .getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }
}
