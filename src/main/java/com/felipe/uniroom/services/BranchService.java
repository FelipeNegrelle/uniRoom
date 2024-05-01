package com.felipe.uniroom.services;

import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.repositories.BranchRepository;
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

    private static String validateBranch(Branch branch, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        if (branch.getCnpj().equals("00000000000000")) {
            errorsSb.append("CNPJ inválido!\n");
        }

        final Set<ConstraintViolation<Branch>> violations = validator.validate(branch);

        if (!violations.isEmpty()) {
            violations.forEach(violation -> errorsSb.append(violation.getMessage().equals("número do registro de contribuinte corporativo brasileiro (CNPJ) inválido") ? "CNPJ Inválido" : violation.getMessage()).append("\n"));
        }

        if (isUpdate) {
            if (BranchRepository.hasDuplicateCnpj(branch.getCnpj(), branch.getIdBranch())) {
                errorsSb.append("CNPJ já cadastrado!\n");
            }
        } else {
            if (Objects.nonNull(BranchRepository.findByCnpj(branch.getCnpj()))) {
                errorsSb.append("CNPJ já cadastrado!\n");
            }
        }

        if (branch.getName().isBlank()) {
            errorsSb.append("Nome da filial não pode ser vazio!\n");
        }

        if (branch.getName().length() > 50) {
            errorsSb.append("Nome da filial deve ter no máximo 50 caracteres!\n");
        }

        if (Objects.isNull(branch.getCorporate())) {
            errorsSb.append("Matriz não pode ser vazia!\n");
        }

        if (Objects.isNull(branch.getUser())) {
            errorsSb.append("Usuário não pode ser vazio!\n");
        }

        return errorsSb.toString();
    }

    public static Boolean save(Branch branch) {
        try {
            branch.setCnpj(branch.getCnpj().replaceAll("[^0-9]", ""));

            final String validations = validateBranch(branch, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
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
            final Branch result = BranchRepository.findById(Branch.class, branch.getIdBranch());

            if (Objects.nonNull(result)) {
                branch.setCnpj(branch.getCnpj().replaceAll("[^0-9]", ""));

                final String validations = validateBranch(branch, true);

                if (!validations.isEmpty()) {
                    JOptionPane.showMessageDialog(null, validations);
                    return false;
                } else {
                    result.setIdBranch(branch.getIdBranch());
                    result.setName(branch.getName());
                    result.setCnpj(branch.getCnpj());
                    result.setCorporate(branch.getCorporate());
                    result.setUser(branch.getUser());
                    result.setActive(branch.getActive());

                    return BranchRepository.saveOrUpdate(branch);
                }
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
        if (Objects.isNull(field) || field.isBlank()) {
            field = "name";
        }

        return BranchRepository.search(Branch.class, search, field);
    }
}
