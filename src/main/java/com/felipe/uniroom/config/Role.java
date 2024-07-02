package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.Menu;
import com.felipe.uniroom.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Role {
    private final Character role;
    private final List<Menu> menus;
    private User user;
    private List<Corporate> corporates;
    private List<Branch> branches;
    private boolean multi;

    public Role(Character role) {
        this.role = role;
        this.menus = getMenusByRole(role);
        this.user = null;
        this.corporates = null;
        this.branches = null;
        this.multi = false;
    }

    public Role(Character role, User user, List<Corporate> corporates, List<Branch> branches) {
        this.role = role;
        this.menus = getMenusByRole(role);
        this.user = user;
        this.corporates = corporates;
        this.branches = branches;
        this.multi = branches.size() > 1;
    }

    public Character getRole() {
        return role;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Corporate> getCorporates() {
        return corporates;
    }

    public void setCorporate(List<Corporate> corporates) {
        this.corporates = corporates;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public boolean getMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public static List<Menu> getMenusByRole(Character role) {
        switch (role) {
            case 'A':
                final List<Menu> menusAdmin = new ArrayList<>();
                Collections.addAll(menusAdmin, Arrays.stream(Menus.values()).map(Menus::getMenu).toArray(Menu[]::new));

                return menusAdmin;
            case 'C':
                final List<Menu> menusCorporate = new ArrayList<>();
                menusCorporate.add(Menus.MENU_USER.getMenu());
                menusCorporate.add(Menus.MENU_CORPORATE.getMenu());
                menusCorporate.add(Menus.MENU_BRANCH.getMenu());
                menusCorporate.add(Menus.MENU_ITEM.getMenu());
                menusCorporate.add(Menus.MENU_RESERVATION.getMenu());
                menusCorporate.add(Menus.MENU_ROOM.getMenu());
                menusCorporate.add(Menus.MENU_ROOM_TYPE.getMenu());
                menusCorporate.add(Menus.MENU_GUEST.getMenu());
                menusCorporate.add(Menus.MENU_SERVICE.getMenu());

                return menusCorporate;
            case 'B':
                final List<Menu> menusBranch = new ArrayList<>();
                menusBranch.add(Menus.MENU_USER.getMenu());
                menusBranch.add(Menus.MENU_BRANCH.getMenu());
                menusBranch.add(Menus.MENU_ITEM.getMenu());
                menusBranch.add(Menus.MENU_RESERVATION.getMenu());
                menusBranch.add(Menus.MENU_ROOM.getMenu());
                menusBranch.add(Menus.MENU_ROOM_TYPE.getMenu());
                menusBranch.add(Menus.MENU_GUEST.getMenu());
                menusBranch.add(Menus.MENU_SERVICE.getMenu());

                return menusBranch;
            case 'E':
                final List<Menu> menusEmployee = new ArrayList<>();
                menusEmployee.add(Menus.MENU_RESERVATION.getMenu());
                menusEmployee.add(Menus.MENU_ROOM.getMenu());
                menusEmployee.add(Menus.MENU_ITEM.getMenu());
                menusEmployee.add(Menus.MENU_GUEST.getMenu());

                return menusEmployee;
            default:
                return Collections.emptyList();
        }
    }
}
