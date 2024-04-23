package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.view.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BranchService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    public static Boolean save(Branch branch) {
        final StringBuilder errorsSb = new StringBuilder();
        try {
            final Set<ConstraintViolation<Branch>> violations = validator.validate(branch);

            if (!violations.isEmpty()) {
                violations.forEach(violation -> errorsSb.append(violation.getMessage()).append("\n"));
            }

            branch.setCnpj(branch.getCnpj().replaceAll("[^0-9]", ""));

            if (BranchRepository.findByCnpj(branch.getCnpj()) != null) {
                errorsSb.append("CNPJ já cadastrado!\n");
            }

            if (branch.getName().length() > 50) {
                errorsSb.append("Nome da filial deve ter no máximo 50 caracteres!\n");
            }

            if (!errorsSb.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorsSb.toString());
                return false;
            } else {
                return BranchRepository.saveOrUpdate(branch);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Components.showGenericError(null);

            return null;
        }
    }

    public static Boolean update(Branch branch) {
        try {
            final Branch result = UserRepository.findById(Branch.class, branch.getIdBranch());

            if (Objects.nonNull(result)) {
                final Set<ConstraintViolation<Branch>> violations = validator.validate(branch);

                if (!violations.isEmpty()) {
                    final StringBuilder violationsSb = new StringBuilder();

                    violations.forEach(violation -> violationsSb.append(violation.getMessage()).append("\n"));

                    JOptionPane.showMessageDialog(null, violationsSb.toString());
                    return false;
                }

                branch.setCnpj(branch.getCnpj().replaceAll("[^0-9]", ""));

                if (!BranchRepository.hasDuplicateCnpj(branch.getCnpj(), branch.getIdBranch())) {
                    result.setIdBranch(branch.getIdBranch());
                    result.setName(branch.getName());
                    result.setCnpj(branch.getCnpj());
                    result.setCorporate(branch.getCorporate());
                    result.setUser(branch.getUser());
                    result.setActive(branch.getActive());
                } else {
                    JOptionPane.showMessageDialog(null, "CNPJ já cadastrado!");

                    return false;
                }

                return BranchRepository.saveOrUpdate(result);
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

    public static Boolean delete(Branch branch) {
        try {
            final Branch result = BranchRepository.findById(Branch.class, branch.getIdBranch());

            if (Objects.nonNull(result)) {
                return BranchRepository.delete(Branch.class, result.getIdBranch());
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

    public static List<Branch> search(String search, String field) {
        if(Objects.isNull(field) || field.isEmpty() || field.isBlank()) {
            field = "name";
        }
        
        return BranchRepository.search(Branch.class, search, field);
    }
}
