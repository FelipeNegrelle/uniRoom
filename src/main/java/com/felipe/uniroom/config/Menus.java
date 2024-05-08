package com.felipe.uniroom.config;

import com.felipe.uniroom.entities.*;
import com.felipe.uniroom.view.*;

public enum Menus {
    MENU_USER(new Menu(Constants.USER, Constants.USER_ICON, UserView.class)),
    MENU_CORPORATE(new Menu(Constants.CORPORATE, Constants.CORPORATE_ICON, CorporateView.class)),
    MENU_BRANCH(new Menu(Constants.BRANCH, Constants.BRANCH_ICON, BranchView.class)),
    MENU_INVENTORY(new Menu(Constants.INVENTORY, Constants.INVENTORY_ICON, InventoryView.class)),
    MENU_ITEM(new Menu(Constants.ITEM, Constants.ITEM_ICON, Item.class)),
    MENU_RESERVATION(new Menu(Constants.RESERVATION, Constants.RESERVATION_ICON, Reservation.class)),
    MENU_ROOM(new Menu(Constants.ROOM, Constants.ROOM_ICON, Room.class)),
    MENU_ROOM_TYPE(new Menu(Constants.ROOM_TYPE, Constants.ROOM_TYPE_ICON, RoomTypeView.class));

    private final Menu menu;

    Menus(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}
