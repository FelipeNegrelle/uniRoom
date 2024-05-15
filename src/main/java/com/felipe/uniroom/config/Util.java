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
}
