package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.views.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CorporateService {

    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    private static String validateCorporate(Corporate corporate, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (corporate.getCnpj().equals("00000000000000")) {
            errorsSb.append("CNPJ inválido!\n");
        }

        final Set<ConstraintViolation<Corporate>> violations = validator.validate(corporate);

        if (!violations.isEmpty()) {
            violations.forEach(violation -> errorsSb.append(violation.getMessage().equals("número do registro de contribuinte corporativo brasileiro (CNPJ) inválido") ? "CNPJ Inválido" : violation.getMessage()).append("\n"));
        }

        if (isUpdate) {
            if (CorporateRepository.hasDuplicateCnpj(corporate.getCnpj(), corporate.getIdCorporate())) {
                errorsSb.append("CNPJ já cadastrado!\n");
            }
        } else {
            if (Objects.nonNull(CorporateRepository.findByCnpj(corporate.getCnpj()))) {
                errorsSb.append("CNPJ já cadastrado!\n");
            }
        }

        if (corporate.getName().isBlank()) {
            errorsSb.append("Nome da corporação não pode ser vazio!\n");
        }

        if (corporate.getName().length() > 50) {
            errorsSb.append("Nome da corporação deve ter no máximo 50 caracteres!\n");
        }

        if (Objects.isNull(corporate.getUser())) {
            errorsSb.append("Usuário não pode ser vazio!\n");
        }

        return errorsSb.toString();
    }

    public static Boolean save(Corporate corporate) {
        try {
            corporate.setCnpj(corporate.getCnpj().replaceAll("[^0-9]", ""));

            final String validations = validateCorporate(corporate, false);

            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);

                return false;
            } else {
                return CorporateRepository.saveOrUpdate(corporate);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return null;
        }
    }

    public static Boolean update(Corporate corporate) {
        try {
            final Corporate result = CorporateRepository.findById(Corporate.class, corporate.getIdCorporate());

            if (Objects.nonNull(result)) {
                corporate.setCnpj(corporate.getCnpj().replaceAll("[^0-9]", ""));

                final String validations = validateCorporate(corporate, true);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);

                    return false;
                } else {
                    result.setIdCorporate(corporate.getIdCorporate());
                    result.setName(corporate.getName());
                    result.setCnpj(corporate.getCnpj());
                    result.setUser(corporate.getUser());
                    result.setActive(corporate.getActive());

                    return CorporateRepository.saveOrUpdate(result);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Matriz não encontrada.");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean delete(Corporate corporate) {
        try {
            final Corporate result = CorporateRepository.findById(Corporate.class, corporate.getIdCorporate());

            if (Objects.nonNull(result)) {
                return CorporateRepository.delete(Corporate.class, result.getIdCorporate());
            } else {
                JOptionPane.showMessageDialog(null, "Filial não encontrada.");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static List<Corporate> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }

        return CorporateRepository.search(Corporate.class, search, field, role);
    }
}