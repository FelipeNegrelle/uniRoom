package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.GuestRepository;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationForm extends JFrame {
    private final List<Guest> guests = new ArrayList<>();
    private final List<Guest> guestsTableList = new ArrayList<>();
    private final List<Branch> branches = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();

    private static DefaultTableModel model;

    public ReservationForm(Role role, Reservation entity) {
        super(Constants.RESERVATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[][grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titlePage = new JLabel(Constants.RESERVATION);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel daysLabel = new JLabel("Dias de estadia:");
        daysLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JTextField daysField = new JTextField(20);
        if (Objects.nonNull(entity))
            daysField.setText(entity.getDays().toString());
        daysField.setPreferredSize(new Dimension(300, 30));
        daysField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel guestsLabel = Components.getLabel(Constants.GUEST + ":", null, Font.BOLD, null, null);
        final JComboBox<String> guestsCombo = new JComboBox<>();
        guestsCombo.setPreferredSize(new Dimension(300, 30));
        guestsCombo.setFont(Constants.FONT);

        model = new DefaultTableModel(new Object[]{"Nome", "CPF"}, 0);

        final JTable guestsTable = new JTable();
        guestsTable.setModel(model);
        guestsTable.setFont(new Font("Sans", Font.PLAIN, 20));
        guestsTable.setSelectionForeground(Color.WHITE);
        guestsTable.setAutoCreateRowSorter(true);
        guestsTable.setRowHeight(30);
        guestsTable.setDefaultEditor(Object.class, null);

        final JButton addGuestButton = new JButton("Adicionar hóspede");
        addGuestButton.addActionListener(e -> {
            final Guest selectedGuest = guests.get(guestsCombo.getSelectedIndex());
            if (!guestsTableList.contains(selectedGuest)) {
                guestsTableList.add(selectedGuest);
                updateGuestsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Hóspede já adicionado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        final JButton removeGuestButton = new JButton("Remover hóspede");
        removeGuestButton.addActionListener(e -> {
            final int selectedRow = guestsTable.getSelectedRow();
            if (selectedRow != -1) {
                guestsTableList.remove(selectedRow);
                updateGuestsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um hóspede para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });


        final JLabel branchesLabel = Components.getLabel(Constants.BRANCH + ":", null, Font.BOLD, null, null);
        final JComboBox<String> branchesCombo = new JComboBox<>();
        branchesCombo.setPreferredSize(new Dimension(300, 30));
        branchesCombo.setFont(Constants.FONT);

        final JLabel roomsLabel = Components.getLabel(Constants.ROOM + ":", null, Font.BOLD, null, null);
        final JComboBox<String> roomsCombo = new JComboBox<>();
        roomsCombo.setPreferredSize(new Dimension(300, 30));
        roomsCombo.setFont(Constants.FONT);

        populateGuestCombo(guestsCombo, entity, role);
        populateBranchCombo(branchesCombo, entity, role);
        populateRoomsCombo(roomsCombo, entity, role);

        inputPanel.add(daysLabel);
        inputPanel.add(daysField, "wrap");
        mainPanel.add(inputPanel, "wrap, grow");
        inputPanel.add(guestsLabel);
        inputPanel.add(guestsCombo);
        inputPanel.add(addGuestButton, "span, split 2");
        inputPanel.add(removeGuestButton, "wrap");
        inputPanel.add(new JScrollPane(guestsTable), "wrap");

        if (!role.getRole().equals('E')) {
            inputPanel.add(branchesLabel);
            inputPanel.add(branchesCombo, "wrap");
        }
        inputPanel.add(roomsLabel);
        inputPanel.add(roomsCombo, "wrap");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final short days;

            try {
                days = Short.parseShort(daysField.getText());
            } catch (
                    NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Dias de estadia inválidos!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            final Reservation reservation = new Reservation();
            reservation.setIdReservation(Objects.nonNull(entity) ? entity.getIdReservation() : null);
            reservation.setDays(days);
            reservation.setBranch(role.getRole().equals('E') ? role.getBranches().getFirst() : (branchesCombo.getItemCount() > 0 ? branches.get(branchesCombo.getSelectedIndex()) : null));
            reservation.setRoom(roomsCombo.getItemCount() > 0 ? rooms.get(roomsCombo.getSelectedIndex()) : null);
            reservation.setUser(Objects.nonNull(entity) ? entity.getUser() : role.getUser());
            reservation.setStatus(Objects.nonNull(entity) ? entity.getStatus() : "CI");

            final Boolean result = Objects.nonNull(entity) ? ReservationService.update(reservation, guests) : ReservationService.save(reservation, guests);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new ReservationView(role);
                dispose();
            }
        });

        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new ReservationView(role);
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

    private void updateGuestsTable() {
        model.setRowCount(0);
        for (Guest guest : guestsTableList) {
            model.addRow(new Object[]{
                    guest.getName(),
                    guest.getCpf()
            });
        }
    }

    private void populateGuestCombo(JComboBox<String> guestCombo, Reservation entity, Role role) {
        final List<Guest> guestList = GuestRepository.findAll(Guest.class, role);

        if (Objects.nonNull(guestList) && !guestList.isEmpty()) {
            guestCombo.removeAllItems();
            guests.clear();

            for (Guest guest : guestList) {
                guestCombo.addItem(guest.getName());
                guests.add(guest);
            }
        }
    }

    private void populateBranchCombo(JComboBox<String> branchCombo, Reservation entity, Role role) {
        final List<Branch> branchList = BranchRepository.findAll(Branch.class, role);

        if (Objects.nonNull(branchList) && !branchList.isEmpty()) {
            branchCombo.removeAllItems();
            branches.clear();

            for (Branch branch : branchList) {
                branchCombo.addItem(branch.getName());
                branches.add(branch);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getBranch())) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }

    private void populateRoomsCombo(JComboBox<String> roomsCombo, Reservation entity, Role role) {
        final List<Room> roomList = RoomRepository.findAll(Room.class, role);

        if (Objects.nonNull(roomList) && !roomList.isEmpty()) {
            roomsCombo.removeAllItems();
            rooms.clear();

            for (Room room : roomList) {
                roomsCombo.addItem(room.getRoomNumber().toString() + " - " + room.getBranch().getName());
                rooms.add(room);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getRoom())) {
            roomsCombo.setSelectedItem(entity.getRoom().getRoomNumber().toString());
        }
    }
}
