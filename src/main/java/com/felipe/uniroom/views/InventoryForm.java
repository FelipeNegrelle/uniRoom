package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.services.InventoryService;
import com.felipe.uniroom.services.RoomService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryForm extends JFrame {
    //    final List<Branch> branches = new ArrayList<>();
    final List<Room> rooms = new ArrayList<>();

    public InventoryForm(Role role, Inventory entity, Room inventoryRoom) {
        super(Constants.INVENTORY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(Constants.INVENTORY);
        titleLabel.setFont(new Font("Sans", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

//        JLabel roomLabel = new JLabel(Constants.ROOM + ":");
//        roomLabel.setFont(new Font("Sans", Font.BOLD, 20));
//        JComboBox<String> roomCombo = new JComboBox<>();
//        roomCombo.setPreferredSize(new Dimension(300, 30));
//        roomCombo.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel branchLabel = new JLabel(Constants.BRANCH + ":");
        branchLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel descriptionLabel = new JLabel(Constants.NAME + ":");
        descriptionLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

//        inputPanel.add(roomLabel);
//        inputPanel.add(roomCombo, "wrap");
//        inputPanel.add(branchLabel);
//        inputPanel.add(branchCombo, "wrap");
        inputPanel.add(descriptionLabel);
        inputPanel.add(nameField, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> {
            final Inventory inventory = new Inventory();
            inventory.setIdInventory(Objects.nonNull(entity) ? entity.getIdInventory() : null);
            inventory.setRoom(inventoryRoom);
            inventory.setBranch(inventoryRoom.getBranch());
            inventory.setDescription(nameField.getText());
            inventory.setActive(true);

            final Boolean result = Objects.nonNull(entity) ? InventoryService.update(inventory) : InventoryService.save(inventory);

            if (result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new InventoryView(role, inventoryRoom);
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
            new InventoryView(role, inventoryRoom);
        });

        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);

//        populateRoomCombo(roomCombo, role, entity);
//        populateBranchCombo(branchCombo, entity, role);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


//    private void populateRoomCombo(JComboBox<String> roomCombo, Role role, Inventory entity) {
//        final List<Room> roomsList = RoomService.findAll(role);
//
//        if (Objects.nonNull(roomsList)) {
//            roomCombo.removeAllItems();
//            rooms.clear();
//
//            for (Room room : roomsList) {
//                roomCombo.addItem(room.getRoomNumber() + " - " + room.getBranch().getName());
//                rooms.add(room);
//            }
//        }
//
//        if (Objects.nonNull(entity)) {
//            for (Room room : rooms) {
//                if (room.getIdRoom().equals(entity.getRoom().getIdRoom())) {
//                    roomCombo.setSelectedItem(room.getRoomNumber() + " - " + room.getBranch().getName());
//                    break;
//                }
//            }
//        }
//    }

//    private void populateBranchCombo(JComboBox<String> branchCombo, Inventory entity, Role role) {
//        final List<Branch> branchList = BranchService.findAll(role);
//
//        if (Objects.nonNull(branchList) && !branchList.isEmpty()) {
//            branchCombo.removeAllItems();
//            branches.clear();
//
//            for (Branch branch : branchList) {
//                branchCombo.addItem(branch.getName());
//                branches.add(branch);
//            }
//        }
//
//        if (Objects.nonNull(entity)) {
//            branchCombo.setSelectedItem(entity.getBranch().getName());
//        }
//    }
}
