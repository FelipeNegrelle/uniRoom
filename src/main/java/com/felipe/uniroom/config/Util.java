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
    public static NumberFormatter getNumberFormatter(int maximumFractionDigits) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMaximumFractionDigits(maximumFractionDigits);
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        return formatter;
    }
}
