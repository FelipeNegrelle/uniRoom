package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.services.GuestService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuestForm extends JFrame {
    final List<Room> rooms = new ArrayList<>();
    final List<Branch> branches = new ArrayList<>();

    public GuestForm(Role role, Guest entity) {
        super(Constants.GUEST);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.GUEST);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel("Nome do Hóspede:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField nameField = new JTextField(20);
        if (Objects.nonNull(entity))
            nameField.setText(entity.getName());
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel cpfLabel = Components.getLabel(Constants.CPF + ": ", null, Font.BOLD, null, null);

        final JFormattedTextField cpfField = new JFormattedTextField(Components.getCpfFormatter());
        if (Objects.nonNull(entity)) {
            cpfField.setText(Util.formatCpf(entity.getCpf()));
        }
        cpfField.setPreferredSize(new Dimension(300, 30));
        cpfField.setFont(Constants.FONT);

        final JLabel hostedLabel = Components.getLabel(Constants.HOSTED + ": ", null, Font.BOLD, null, null);
        final JCheckBox hostedCheck = new JCheckBox();
        hostedCheck.setPreferredSize(new Dimension(50, 50));
        hostedCheck.setSelected(Objects.nonNull(entity) && entity.getHosted());

        final JLabel branchLabel = Components.getLabel(Constants.BRANCH + ": ", null, Font.BOLD, null, null);
        final JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(Constants.FONT);

        final JLabel roomLabel = Components.getLabel(Constants.ROOM + ": ", null, Font.BOLD, null, null);
        final JComboBox<String> roomCombo = new JComboBox<>();
        roomCombo.setPreferredSize(new Dimension(300, 30));
        roomCombo.setFont(Constants.FONT);

//        populateRoomCombo(roomCombo, entity, role);
        populateBranchCombo(branchCombo, entity, role);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");

        inputPanel.add(cpfLabel);
        inputPanel.add(cpfField, "wrap");

//        inputPanel.add(hostedLabel);
//        inputPanel.add(hostedCheck, "wrap");

//        inputPanel.add(roomLabel);
//        inputPanel.add(roomCombo, "wrap");
        if(!role.getRole().equals('E')) {
            inputPanel.add(branchLabel);
            inputPanel.add(branchCombo, "wrap");
        }

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final Guest guest = new Guest();
            guest.setIdGuest(Objects.nonNull(entity) ? entity.getIdGuest() : null);
            guest.setName(nameField.getText());
            guest.setCpf(cpfField.getText());
//            guest.setRoom(rooms.get(roomCombo.getSelectedIndex()));
            guest.setRoom(null);
            guest.setBranch(role.getRole().equals('E') ? role.getBranches().getFirst() : branches.get(branchCombo.getSelectedIndex()));
            guest.setHosted(Objects.nonNull(entity) ? guest.getHosted() : false);

            final Boolean result = Objects.nonNull(entity) ? GuestService.update(guest) : GuestService.save(guest);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new GuestView(role);
                dispose();
            }
        });

        final JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new GuestView(role);
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

    private void populateRoomCombo(JComboBox<String> roomCombo, Guest entity, Role role) {
        final List<Room> roomList = RoomRepository.findAll(Room.class, role);

        if (Objects.nonNull(roomList) && !roomList.isEmpty()) {
            roomCombo.removeAllItems();
            rooms.clear();

            for (Room room : roomList) {
                roomCombo.addItem(room.getRoomNumber().toString());
                rooms.add(room);
            }
        }

        if (Objects.nonNull(entity)) {
            roomCombo.setSelectedItem(entity.getRoom().getRoomNumber());
        }
    }

    private void populateBranchCombo(JComboBox<String> branchCombo, Guest entity, Role role) {
        final List<Branch> branchList = BranchRepository.findAll(Branch.class, role);

        if (Objects.nonNull(branchList) && !branchList.isEmpty()) {
            branchCombo.removeAllItems();
            branches.clear();

            for (Branch branch : branchList) {
                branchCombo.addItem(branch.getName());
                branches.add(branch);
            }
        }

        if (Objects.nonNull(entity)) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }
}

