package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.InventoryItem;

import javax.swing.*;

public class InventoryItemForm extends JFrame {
    public InventoryItemForm(Role role, InventoryItem inventoryItem) {
        super("Inventory Item Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }
}
