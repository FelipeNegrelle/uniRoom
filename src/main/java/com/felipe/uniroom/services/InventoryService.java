package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.repositories.InventoryRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.Set;

public class InventoryService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private static String validateInventory(Inventory inventory, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        final Set<ConstraintViolation<Inventory>> violations = validator.validate(inventory);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Inventory> violation : violations) {
                errorsSb.append(violation.getMessage()).append("\n");
            }
        }

        if (inventory.getDescription().isBlank()) {
            errorsSb.append("Descrição do inventário não pode ser vazia!\n");
        }

        if (inventory.getDescription().length() > 255) {
            errorsSb.append("Descrição do inventário deve ter no máximo 255 caracteres!\n");
        }

        if (inventory.getRoom() == null) {
            errorsSb.append("O inventário deve estar associado a um quarto!\n");
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o inventário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
