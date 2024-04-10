package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.User;

public class Util {
    public static Role getRoleUser(User user) {
        return switch (user.getRole()) {
            case 'A' -> Role.ADMIN;
            case 'M' -> Role.MANAGER;
            case 'E' -> Role.EMPLOYEE;
            default -> null;
        };
    }
}
