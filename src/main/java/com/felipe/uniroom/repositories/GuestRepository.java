package com.felipe.uniroom.repositories;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Guest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class GuestRepository extends DatabaseRepository {
    public static Guest findByCpf(String cpf, Role role) {
        final String query = "SELECT g FROM Guest g WHERE g.cpf = :cpf AND g.branch IN :branches";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class)
                    .setParameter("cpf", cpf)
                    .setParameter("branches", role.getBranches())
                    .getSingleResult();
        } catch (
                NoResultException e) {
            return null;
        }
    }

    public static Boolean hasDuplicateCpf(String cpf, Integer idGuest, Role role) {
        final String query = "SELECT g FROM Guest g WHERE g.cpf = :cpf and g.idGuest <> :idGuest AND g.branch IN :branches";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class)
                    .setParameter("cpf", cpf)
                    .setParameter("idGuest", idGuest)
                    .setParameter("branches", role.getBranches())
                    .getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }

    public static Boolean hasDuplicateName(String name, Integer idGuest) {
        final String query = "SELECT g FROM Guest g WHERE g.name = :name and g.idGuest <> :idGuest";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class).setParameter("name", name).setParameter("idGuest", idGuest).getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }

    public static Guest findByName(String name) {
        final String query = "SELECT g FROM Guest g WHERE g.name = :name";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class).setParameter("name", name).getSingleResult();
        } catch (
                NoResultException e) {
            return null;
        }
    }

    public static Guest findByPassport(String passport) {
        final String query = "SELECT g FROM Guest g WHERE g.passportNumber = :passport";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class).setParameter("passport", passport).getSingleResult();
        } catch (
                NoResultException e) {
            return null;
        }
    }

    public static Boolean hasDuplicatePassport(String passport, Integer idGuest) {
        final String query = "SELECT g FROM Guest g WHERE g.passportNumber = :passport and g.idGuest <> :idGuest";

        try (EntityManager em = ConnectionManager.getEntityManager()) {
            return em.createQuery(query, Guest.class).setParameter("passport", passport).setParameter("idGuest", idGuest).getSingleResult() != null;
        } catch (
                NoResultException e) {
            return false;
        }
    }
}
