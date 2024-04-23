package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.view.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.Objects;
import java.util.Set;

public class CorporateService {

    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    public static Boolean save(Corporate Corporate) {
        try {
            final Set<ConstraintViolation<Corporate>> violations = validator.validate(Corporate);

            if (!violations.isEmpty()) {
                final StringBuilder violationsSb = new StringBuilder();

                violations.forEach(violation -> violationsSb.append(violation.getMessage()).append("\n"));

                JOptionPane.showMessageDialog(null, violationsSb.toString());
                return false;
            }

            Corporate.setCnpj(Corporate.getCnpj().replaceAll("[^0-9]", ""));

            if (CorporateRepository.findByCnpj(Corporate.getCnpj()) != null) {
                JOptionPane.showMessageDialog(null, "CNPJ já cadastrado!");

                return false;
            } else {
                return CorporateRepository.saveOrUpdate(Corporate);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return null;
        }
    }

    public static Boolean update(Corporate Corporate) {
        try {
            final Corporate result = UserRepository.findById(Corporate.class, Corporate.getIdCorporate());

            if (Objects.nonNull(result)) {
                final Set<ConstraintViolation<Corporate>> violations = validator.validate(Corporate);

                if (!violations.isEmpty()) {
                    final StringBuilder violationsSb = new StringBuilder();

                    violations.forEach(violation -> violationsSb.append(violation.getMessage()).append("\n"));

                    JOptionPane.showMessageDialog(null, violationsSb.toString());
                    return false;
                }

                Corporate.setCnpj(Corporate.getCnpj().replaceAll("[^0-9]", ""));

                if (!CorporateRepository.hasDuplicateCnpj(Corporate.getCnpj(), Corporate.getIdCorporate())) {
                    result.setIdCorporate(Corporate.getIdCorporate());
                    result.setName(Corporate.getName());
                    result.setCnpj(Corporate.getCnpj());
                    result.setUser(Corporate.getUser());
                    result.setActive(Corporate.getActive());
                } else {
                    JOptionPane.showMessageDialog(null, "CNPJ já cadastrado!");

                    return false;
                }

                return CorporateRepository.saveOrUpdate(result);
            } else {
                JOptionPane.showMessageDialog(null, "Filial não encontrada!");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }

    public static Boolean delete(Corporate Corporate) {
        try {
            final Corporate result = CorporateRepository.findById(Corporate.class, Corporate.getIdCorporate());

            if (Objects.nonNull(result)) {
                return CorporateRepository.delete(Corporate.class, result.getIdCorporate());
            } else {
                JOptionPane.showMessageDialog(null, "Filial não encontrada!");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return false;
        }
    }
}
