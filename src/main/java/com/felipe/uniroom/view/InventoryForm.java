package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.services.InventoryService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class InventoryForm extends JFrame {
    public InventoryForm(Role role, Inventory entity) {
        super(Constants.INVENTORY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(Constants.INVENTORY);
        titleLabel.setFont(new Font("Sans", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel roomLabel = new JLabel(Constants.ROOM + ":");
        roomLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JComboBox<Room> roomCombo = new JComboBox<>();
        roomCombo.setPreferredSize(new Dimension(300, 30));
        roomCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        populateRoomCombo(roomCombo);

        JLabel descriptionLabel = new JLabel(Constants.NAME + ":");
        descriptionLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        if (Objects.nonNull(entity)) {
            roomCombo.setSelectedItem(entity.getRoom());
            nameField.setText(entity.getDescription());
        }

        inputPanel.add(roomLabel);
        inputPanel.add(roomCombo, "wrap");
        inputPanel.add(descriptionLabel);
        inputPanel.add(nameField, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);

        // Final reference to entity for use in lambda
        final Inventory finalEntity = entity == null ? new Inventory() : entity;

        saveButton.addActionListener(e -> {
            finalEntity.setRoom((Room) roomCombo.getSelectedItem());
            finalEntity.setDescription(nameField.getText());
            finalEntity.setActive(true);

            if (InventoryService.save(finalEntity)) {
                JOptionPane.showMessageDialog(this, "Inventário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new InventoryView(role);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o inventário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            dispose();
            new InventoryView(role);
        });

        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void populateRoomCombo(JComboBox<Room> roomCombo) {
        List<Room> rooms = RoomRepository.findAll(Room.class);
        rooms.forEach(roomCombo::addItem);
    }

}
