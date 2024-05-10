package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.view.Components;

import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Util {
    public static Role getRoleUser(User user) {
        return switch (user.getRole()) {
            case 'A' -> Role.ADMIN;
            case 'M' -> Role.MANAGER;
            case 'E' -> Role.EMPLOYEE;
            default -> null;
        };
    }

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
    public static NumberFormat getNumberFormatter(int decimalPlaces) {
        String pattern = "#,##0";
        if (decimalPlaces > 0) {
            pattern += ".";
            for (int i = 0; i < decimalPlaces; i++) {
                pattern += "0";
            }
        }
        return new DecimalFormat(pattern);
    }
}
