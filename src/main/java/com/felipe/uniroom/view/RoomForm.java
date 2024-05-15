package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.entities.RoomType;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.services.RoomService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomForm extends JFrame {
    private List<RoomType> roomTypes = new ArrayList<>();
    private List<Branch> branches = new ArrayList<>();

    public RoomForm(Role role, Room entity) {
        super(Constants.ROOM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.ROOM);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel numberLabel = new JLabel("Número do Quarto:");
        numberLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JFormattedTextField numberField = new JFormattedTextField(Util.getNumberFormatter(0));
        if (Objects.nonNull(entity)) numberField.setValue(entity.getRoomNumber());
        numberField.setPreferredSize(new Dimension(300, 30));
        numberField.setFont(new Font("Sans", Font.PLAIN, 20));
        numberField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        final JLabel branchLabel = new JLabel("Filial:");
        branchLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        populateBranchCombo(branchCombo, entity);

        final JLabel roomTypeLabel = new JLabel("Tipo de Quarto:");
        roomTypeLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JComboBox<String> roomTypeCombo = new JComboBox<>();
        roomTypeCombo.setPreferredSize(new Dimension(300, 30));
        roomTypeCombo.setFont(new Font("Sans", Font.PLAIN, 20));

        if (branchCombo.getItemCount() > 0) {
            branchCombo.setSelectedIndex(0);
        }

        branchCombo.addActionListener(e -> {
            if (branchCombo.getSelectedIndex() != -1) {
                Branch selectedBranch = branches.get(branchCombo.getSelectedIndex());
                populateRoomTypeCombo(roomTypeCombo, selectedBranch.getIdBranch(), entity);
            }
        });

        if (branchCombo.getSelectedIndex() != -1) {
            populateRoomTypeCombo(roomTypeCombo, branches.get(branchCombo.getSelectedIndex()).getIdBranch(), entity);
        }

        inputPanel.add(numberLabel);
        inputPanel.add(numberField, "wrap");
        inputPanel.add(branchLabel);
        inputPanel.add(branchCombo, "wrap");
        inputPanel.add(roomTypeLabel);
        inputPanel.add(roomTypeCombo, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            Room room = new Room();
            room.setIdRoom(Objects.nonNull(entity) ? entity.getIdRoom() : null);
            String numberStr = numberField.getText().trim();
            if (!numberStr.isEmpty()) {
                room.setRoomNumber(Integer.parseInt(numberStr));
            } else {
                room.setRoomNumber(null);
            }
            room.setBranch(branches.get(branchCombo.getSelectedIndex()));
            room.setRoomType(roomTypes.get(roomTypeCombo.getSelectedIndex()));
            room.setActive(true);

            boolean result = Objects.nonNull(entity) ? RoomService.update(room) : RoomService.save(room);

            if (result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new RoomView(role);
                dispose();
            }
        });

        final JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new RoomView(role);
            dispose();
        });

        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void populateBranchCombo(JComboBox<String> branchCombo, Room entity) {
        List<Branch> branchList = BranchRepository.findAll(Branch.class);
        branchCombo.removeAllItems();
        branches.clear();
        for (Branch branch : branchList) {
            branchCombo.addItem(branch.getName());
            branches.add(branch);
        }
        if (Objects.nonNull(entity) && entity.getBranch() != null) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }

    private void populateRoomTypeCombo(JComboBox<String> roomTypeCombo, Integer branchId, Room entity) {
        List<RoomType> roomTypeList = RoomRepository.findByBranchId(branchId);
        roomTypeCombo.removeAllItems();
        roomTypes.clear();
        for (RoomType roomType : roomTypeList) {
            roomTypeCombo.addItem(roomType.getName());
            roomTypes.add(roomType);
        }
        if (Objects.nonNull(entity) && entity.getBranch() != null) {
            roomTypeCombo.setSelectedItem(entity.getRoomType().getName());
        }
    }
}
