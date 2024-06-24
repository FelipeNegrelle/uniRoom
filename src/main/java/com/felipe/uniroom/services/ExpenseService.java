package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.repositories.ExpenseRepository;
import com.felipe.uniroom.views.Components;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public class ExpenseService {
    private static String validateExpense(Expense expense, boolean isUpdate, char type) {
        final StringBuilder errorsSb = new StringBuilder();

        if (Objects.equals(type, 'I')) {
            if (Objects.isNull(expense.getItem())) {
                errorsSb.append("Item não pode ser vazio!\n");
            }

            if (Objects.nonNull(expense.getItem()) && ExpenseRepository.hasDuplicateItem(expense)) {
                errorsSb.append("Item já cadastrado!\n");
            }
        } else {
            if (Objects.isNull(expense.getService())) {
                errorsSb.append("Serviço não pode ser vazio!\n");
            }

            if (Objects.nonNull(expense.getService()) && ExpenseRepository.hasDuplicateService(expense)) {
                errorsSb.append("Serviço já cadastrado!\n");
            }
        }

        if (Objects.isNull(expense.getAmount()) || expense.getAmount() <= 0) {
            errorsSb.append("Quantidade deve ser maior que zero!\n");
        }

//        if (expense.getBranch() == null) {
//            errorsSb.append("A despesa deve estar associada a uma filial!\n");
//        }

        if (isUpdate && ExpenseRepository.findById(Expense.class, expense.getIdExpense()) == null) {
            errorsSb.append("Despesa não encontrada!\n");
        }

        return errorsSb.toString();
    }

    public static List<Expense> findAll(Role role){
        return ExpenseRepository.findAll(Expense.class, role);
    }

    public static Expense findById(int id){
        return ExpenseRepository.findById(Expense.class,id);
    }

    public static boolean save(Expense expense, char type) {
        try {
            final String validations = validateExpense(expense, false, type);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ExpenseRepository.saveOrUpdate(expense);
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static boolean update(Expense expense, char type) {
        try {
            final String validations = validateExpense(expense, true, type);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            }
            return ExpenseRepository.saveOrUpdate(expense);
        } catch (
                Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar a depesa: " + e.getMessage());
            return false;
        }
    }

    public static Boolean delete(Expense expense) {
        try {
            final Expense result = ExpenseRepository.findById(Expense.class, expense.getIdExpense());
            if (Objects.nonNull(result)) {
                return ExpenseRepository.delete(Expense.class, result.getIdExpense());
            } else {
                JOptionPane.showMessageDialog(null, "Despesa não encontrada!");
                return false;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return false;
        }
    }

    public static List<Expense> search(String search, String field, Role role) {
        if (Objects.isNull(field) || field.isBlank()) {
            field = "branch.name";
        }

        return ExpenseRepository.search(Expense.class, search, field, role);
    }
}
