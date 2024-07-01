package com.felipe.uniroom.config;

import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String formatCnpj(String cnpj) {
        try {
            final MaskFormatter mask = new MaskFormatter("##.###.###/####-##");

            mask.setValueContainsLiteralCharacters(false);

            return mask.valueToString(cnpj);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatCpf(String cpf) {
        try {
            final MaskFormatter mask = new MaskFormatter("###.###.###-##");

            mask.setValueContainsLiteralCharacters(false);

            return mask.valueToString(cpf);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String maskCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return "";
        }
        // Máscara para o CPF
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + ".***.**";
    }

    public static NumberFormat getNumberFormatter(int decimalPlaces) {
        StringBuilder pattern = new StringBuilder("#,##0");
        if (decimalPlaces > 0) {
            pattern.append(".");
            pattern.append("0".repeat(decimalPlaces));
        }
        return new DecimalFormat(pattern.toString());
    }

    public static String formatNullableDate(Date date, String pattern) {
        if (date == null) {
            return "-";
        }
        return new SimpleDateFormat(pattern).format(date);
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
            case "P" ->
                    "Pendente";
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
            case "Gerente de Estabelecimento" ->
                    'B';
            case "Funcionário" ->
                    'E';
            default ->
                    '?';
        };
    }

    public static String convertRoleName(Character role) {
        return switch (role) {
            case 'A' ->
                    "Administrador";
            case 'C' ->
                    "Gerente de Matriz";
            case 'B' ->
                    "Gerente de Estabelecimento";
            case 'E' ->
                    "Funcionário";
            default ->
                    "Desconhecido";
        };
    }

    public static String[] getRoleCombo(Character role) {
        return switch (role) {
            case 'A' ->
                    new String[]{"Administrador", "Gerente de Matriz", "Gerente de Estabelecimento", "Funcionário"};
            case 'C' ->
                    new String[]{"Gerente de Matriz", "Gerente de Estabelecimento", "Funcionário"};
            case 'B' ->
                    new String[]{"Gerente de Estabelecimento", "Funcionário"};
            default ->
                    new String[]{};
        };
    }

    public static Color getColorReservation(String status) {
        return switch (status) {
            case "Cancelada" ->
                    Constants.RED;
            case "Check-In" ->
                    Constants.YELLOW;
            case "Check-out" ->
                    Constants.GREEN;
            case "Hospedado" ->
                    Constants.PURPLE;
            case "Pendente" ->
                    Constants.GRAY;
            default ->
                    Constants.WHITE;
        };
    }
}
