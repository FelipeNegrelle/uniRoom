package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Service;
import com.felipe.uniroom.repositories.ServiceRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class ServiceService {
    private static String validateService(Service service, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (service.getDescription().isBlank()) {
            errorsSb.append("Descrição do serviço não pode ser vazia.\n");
        }

        if (service.getPrice() == null || service.getPrice() <= 0) {
            errorsSb.append("Preço do serviço deve ser maior que zero.\n");
        }

        if (service.getBranch() == null) {
            errorsSb.append("O item deve estar associado a uma filial.\n");
        }

        return errorsSb.toString();
    }

    public static boolean save(Service service) {
        try {
            final String validations = validateService(service, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ServiceRepository.saveOrUpdate(service);
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static boolean update(Service service) {
        try {
            final String validations = validateService(service, true);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ServiceRepository.saveOrUpdate(service);
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o serviço: " + e.getMessage());
            return false;
        }
    }

    public static Boolean delete(Service service) {
        try {
            final Service result = ServiceRepository.findById(Service.class, service.getIdService());
            if (Objects.nonNull(result)) {
                return ServiceRepository.delete(Service.class, result.getIdService());
            } else {
                JOptionPane.showMessageDialog(null, "Serviço não encontrado!");
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<Service> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "description";
        }

        return ServiceRepository.search(Service.class, search, field, role);
    }
}
