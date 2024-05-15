package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationForm extends JFrame {
    List<User> users = new ArrayList<>();
    List<Branch> branches = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();

    public ReservationForm(Role role, Reservation entity) {
        super(Constants.RESERVATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE); // Cor de fundo semelhante ao Constants.BLUE

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
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

        final JLabel userLabel = Components.getLabel(Constants.USER + ":", null, Font.BOLD, null, null);
        final JComboBox<String> userCombo = new JComboBox<>();
        userCombo.setPreferredSize(new Dimension(300, 30));
        userCombo.setFont(Constants.FONT);

        final JLabel branchesLabel = Components.getLabel(Constants.BRANCH + ":", null, Font.BOLD, null, null);
        final JComboBox<String> branchesCombo = new JComboBox<>();
        branchesCombo.setPreferredSize(new Dimension(300, 30));
        branchesCombo.setFont(Constants.FONT);

        final JLabel roomsLabel = Components.getLabel(Constants.ROOM + ":", null, Font.BOLD, null, null);
        final JComboBox<String> roomsCombo = new JComboBox<>();
        roomsCombo.setPreferredSize(new Dimension(300, 30));
        roomsCombo.setFont(Constants.FONT);

        populateUserCombo(userCombo, entity);
        populateBranchCombo(branchesCombo, entity);
        populateRoomsCombo(roomsCombo, entity);

        inputPanel.add(daysLabel);
        inputPanel.add(daysField, "wrap");
        mainPanel.add(inputPanel, "wrap, grow");
        inputPanel.add(userLabel);
        inputPanel.add(userCombo, "wrap");
        inputPanel.add(branchesLabel);
        inputPanel.add(branchesCombo, "wrap");
        inputPanel.add(roomsLabel);
        inputPanel.add(roomsCombo, "wrap");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            System.out.println(branchesCombo.getItemCount());
            System.out.println(roomsCombo.getItemCount());
            System.out.println(userCombo.getItemCount());
            final Reservation reservation = new Reservation();
            reservation.setIdReservation(Objects.nonNull(entity) ? entity.getIdReservation() : null);
            reservation.setDays(Short.parseShort(daysField.getText()));//todo fazer aceitar somente numeros e do tamanho permitido pelo short
            reservation.setBranch(branchesCombo.getItemCount() > 0 ? branches.get(branchesCombo.getSelectedIndex()) : null);
            reservation.setRoom(roomsCombo.getItemCount() > 0 ? rooms.get(roomsCombo.getSelectedIndex()) : null);
            reservation.setUser(userCombo.getItemCount() > 0 ? users.get(userCombo.getSelectedIndex()) : null);
            reservation.setStatus(Objects.nonNull(entity) ? entity.getStatus() : "CI");

            final Boolean result = Objects.nonNull(entity) ? ReservationService.update(reservation) : ReservationService.save(reservation);

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

    private void populateUserCombo(JComboBox<String> userCombo, Reservation entity) {
        final List<User> userList = UserRepository.findAll(User.class);

        if (Objects.nonNull(userList)) {
            userCombo.removeAllItems();

            for (User user : userList) {
                userCombo.addItem(user.getName());
                users.add(user);
            }
        }

        if (Objects.nonNull(entity)) {
            userCombo.setSelectedItem(entity.getUser().getName());
        }
    }

    private void populateBranchCombo(JComboBox<String> branchCombo, Reservation entity) {
        final List<Branch> branchList = BranchRepository.findAll(Branch.class);

        if (Objects.nonNull(branchList)) {
            branchCombo.removeAllItems();

            for (Branch branch : branchList) {
                branchCombo.addItem(branch.getName());
                branches.add(branch);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getBranch())) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }

    private void populateRoomsCombo(JComboBox<String> roomsCombo, Reservation entity) {
        final List<Room> roomList = RoomRepository.findAll(Room.class);

        if (Objects.nonNull(roomList)) {
            roomsCombo.removeAllItems();

            for (Room room : roomList) {
                roomsCombo.addItem(room.getRoomNumber().toString());
                rooms.add(room);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getRoom())) {
            roomsCombo.setSelectedItem(entity.getRoom().getRoomNumber().toString());
        }
    }
}
