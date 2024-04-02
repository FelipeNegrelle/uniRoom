package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Role {
    ADMIN('A'), MANAGER('M'), EMPLOYEE('E');

    private final char role;
    private final List<Menu> menus;

    Role(char role) {
        this.role = role;
        this.menus = getMenusByRole(role);
    }

    public char getRole() {
        return role;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public static List<Menu> getMenusByRole(char role) {
        switch (role) {
            case 'A':
                final List<Menu> menusAdmin = new ArrayList<>();
                Collections.addAll(menusAdmin, Arrays.stream(Menus.values()).map(Menus::getMenu).toArray(Menu[]::new));

                return menusAdmin;
            case 'M':
                final List<Menu> menusManager = new ArrayList<>();
                menusManager.add(Menus.MENU_CORPORATE.getMenu());
                menusManager.add(Menus.MENU_BRANCH.getMenu());
                menusManager.add(Menus.MENU_INVENTORY.getMenu());
                menusManager.add(Menus.MENU_ITEM.getMenu());
                menusManager.add(Menus.MENU_RESERVATION.getMenu());
                menusManager.add(Menus.MENU_ROOM.getMenu());
                menusManager.add(Menus.MENU_ROOM_TYPE.getMenu());

                return menusManager;
            case 'E':
                final List<Menu> menusEmployee = new ArrayList<>();
                menusEmployee.add(Menus.MENU_RESERVATION.getMenu());
                menusEmployee.add(Menus.MENU_ROOM.getMenu());
                menusEmployee.add(Menus.MENU_INVENTORY.getMenu());
                menusEmployee.add(Menus.MENU_ITEM.getMenu());

                return menusEmployee;
            default:
                return Collections.emptyList();
        }
    }
}
