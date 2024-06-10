package com.felipe.uniroom.config;

import javax.swing.text.MaskFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Util {
    public static String formatCnpj(String cnpj) {
        try {
            final MaskFormatter mask = new MaskFormatter("##.###.###/####-##");

            mask.setValueContainsLiteralCharacters(false);

            return mask.valueToString(cnpj);
        } catch (
                Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatCpf(String cpf) {
        try {
            final MaskFormatter mask = new MaskFormatter("###.###.###-##");

            mask.setValueContainsLiteralCharacters(false);

            return mask.valueToString(cpf);
        } catch (
                Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static NumberFormat getNumberFormatter(int decimalPlaces) {
        StringBuilder pattern = new StringBuilder("#,##0");
        if (decimalPlaces > 0) {
            pattern.append(".");
            pattern.append("0".repeat(decimalPlaces));
        }
        return new DecimalFormat(pattern.toString());
    }

    public static String convertStatusReservation(String status) {
        return switch (status) {
            case "C" ->
                    "Cancelada";
            case "CI" ->
                    "Check-In";
            case "CO" ->
                    "Check-out";
            case "H" ->
                    "Hospedado";
            default ->
                    "Situação Desconhecida";
        };
    }

    public static Character convertRoleName(String roleName) {
        return switch (roleName) {
            case "Administrador" ->
                    'A';
            case "Gerente de Matriz" ->
                    'C';
            case "Gerente de Filial" ->
                    'B';
            case "Funcionário" ->
                    'E';
            default ->
                    '?';
        };
    }

    public static String[] getRoleCombo(Character role) {
        return switch (role) {
            case 'A' ->
                    new String[]{"Administrador", "Gerente de Matriz", "Gerente de Filial", "Funcionário"};
            case 'C' ->
                    new String[]{"Gerente de Matriz", "Gerente de Filial", "Funcionário"};
            case 'B' ->
                    new String[]{"Gerente de Filial", "Funcionário"};
            default ->
                    new String[]{};
        };
    }
}
