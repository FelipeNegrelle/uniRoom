package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.InventoryItem;
import com.felipe.uniroom.entities.InventoryItemId;
import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.ItemRepository;
import com.felipe.uniroom.services.InventoryItemService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryItemForm extends JFrame {
    final List<Item> items = new ArrayList<>();

    public InventoryItemForm(Role role, Inventory inventory, InventoryItem entity) {
        super(Constants.INVENTORY_ITEM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(Constants.INVENTORY_ITEM);
        titleLabel.setFont(new Font("Sans", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel itemLabel = new JLabel(Constants.ITEM + ":");
        itemLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JComboBox<String> itemCombo = new JComboBox<>();
        itemCombo.setPreferredSize(new Dimension(300, 30));
        itemCombo.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel quantityLabel = new JLabel(Constants.QUANTITY + ":");
        quantityLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField quantityField = new JTextField(20);
        quantityField.setPreferredSize(new Dimension(300, 30));
        quantityField.setFont(new Font("Sans", Font.PLAIN, 20));

        if (Objects.nonNull(entity)) {
            quantityField.setText(entity.getQuantity().toString());
        }

        inputPanel.add(itemLabel);
        inputPanel.add(itemCombo, "wrap");
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> {
            final InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setId(new InventoryItemId(inventory.getIdInventory(), items.get(itemCombo.getSelectedIndex()).getIdItem()));

            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (
                    NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida", Constants.WARN, JOptionPane.PLAIN_MESSAGE);
                return;
            }
            inventoryItem.setQuantity(quantity);
            final Boolean result = Objects.nonNull(entity) ? InventoryItemService.update(inventoryItem) : InventoryItemService.save(inventoryItem);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new InventoryItemView(role, inventory);
                dispose();
            }
        });

        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            dispose();
            new InventoryItemView(role, inventory);
        });

        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);

        populateItemCombo(itemCombo, entity, role);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


    private void populateItemCombo(JComboBox<String> itemCombo, InventoryItem entity, Role role) {
        final List<Item> itemList = ItemRepository.findAll(Item.class, role);
        if (Objects.nonNull(itemList)) {
            itemCombo.removeAllItems();
            items.clear();

            for (Item item : itemList) {
                itemCombo.addItem(item.getName() + " - " + item.getBranch().getName());
                items.add(item);
            }
        }
        if (Objects.nonNull(entity)) {
            for (Item item : items) {
                if (item.getIdItem().equals(entity.getId().getIdItem())) {
                    itemCombo.setSelectedItem(item.getName() + " - " + item.getBranch().getName());
                    break;
                }
            }
        }
    }
}
