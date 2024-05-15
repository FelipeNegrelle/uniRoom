package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Role {
    private final char role;
    private final List<Menu> menus;
    private Corporate corporate;
    private Branch branch;

    public Role(char role) {
        this.role = role;
        this.menus = getMenusByRole(role);
        this.corporate = null;
        this.branch = null;
    }

    public Role(char role, Corporate corporate, Branch branch) {
        this.role = role;
        this.menus = getMenusByRole(role);
        this.corporate = corporate;
        this.branch = branch;
    }

    public char getRole() {
        return role;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public Corporate getCorporate() {
        return corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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
