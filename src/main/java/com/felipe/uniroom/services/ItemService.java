package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.ItemRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.Set;

public class ItemService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private static String validateItem(Item item, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        // Perform automated validation based on annotations
        final Set<ConstraintViolation<Item>> violations = validator.validate(item);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Item> violation : violations) {
                errorsSb.append(violation.getMessage()).append("\n");
            }
        }

        // Manual checks
        if (item.getPrice() == null || item.getPrice() <= 0) {
            errorsSb.append("Preço do item deve ser maior que zero.\n");
        }

        if (item.getBranch() == null) {
            errorsSb.append("O item deve estar associado a uma filial.\n");
        }

        if (item.getActive() == null) {
            errorsSb.append("O status ativo do item não pode ser nulo.\n");
        }

        return errorsSb.toString();
    }

    public static Boolean saveOrUpdate(Item item) {
        String validations = validateItem(item, item.getIdItem() != null);

        if (!validations.isEmpty()) {
            JOptionPane.showMessageDialog(null, validations, "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            return ItemRepository.saveOrUpdate(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar ou atualizar o item: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}
