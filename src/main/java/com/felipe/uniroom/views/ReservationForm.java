package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.services.GuestService;
import com.felipe.uniroom.services.ReservationService;
import com.felipe.uniroom.services.RoomService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReservationForm extends JFrame {
    private final List<Guest> guests = new ArrayList<>();
    private final List<Guest> guestsTableList = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();

    private static DefaultTableModel model;
    private JFormattedTextField checkInField;
    private JFormattedTextField checkOutField;
    private JComboBox<String> roomsCombo;
    private JComboBox<String> guestsCombo;

    public ReservationForm(Role role, Reservation entity) {
        super(Constants.RESERVATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = createMainPanel();
        JLabel titlePage = createTitleLabel();
        add(titlePage, "align center, wrap");

        JPanel inputPanel = createInputPanel(entity, role);
        mainPanel.add(inputPanel, "wrap, grow");

        if (Objects.nonNull(entity)) {
            loadReservationGuests(entity);
        }

        JButton saveButton = createSaveButton(role, entity);
        JButton cancelButton = createCancelButton(role);

        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[][grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);
        return mainPanel;
    }

    private JLabel createTitleLabel() {
        JLabel titlePage = new JLabel(Constants.RESERVATION);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        return titlePage;
    }

    private JPanel createInputPanel(Reservation entity, Role role) {
        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel checkInLabel = new JLabel("Data de entrada:");
        checkInLabel.setFont(new Font("Sans", Font.BOLD, 20));
        checkInField = createFormattedDateField();
        checkInField.setPreferredSize(new Dimension(400, 30));
        checkInField.setFont(Constants.FONT);

        JLabel checkOutLabel = new JLabel("Data de saída:");
        checkOutLabel.setFont(new Font("Sans", Font.BOLD, 20));
        checkOutField = createFormattedDateField();
        checkOutField.setPreferredSize(new Dimension(400, 30));
        checkOutField.setFont(Constants.FONT);

        setInitialDates(entity);

        addDateFieldListeners();

        JLabel guestsLabel = createLabel(Constants.GUEST + ":");
        guestsCombo = new JComboBox<>();
        guestsCombo.setPreferredSize(new Dimension(300, 30));
        guestsCombo.setFont(Constants.FONT);

        populateGuestCombo(role);

        model = new DefaultTableModel(new Object[]{"Nome", "CPF", "Passaporte"}, 0);
        JTable guestsTable = createGuestsTable();
        JButton addGuestButton = createAddGuestButton();
        JButton removeGuestButton = createRemoveGuestButton(guestsTable);

        JLabel roomsLabel = createLabel(Constants.ROOM + ":");
        roomsCombo = new JComboBox<>();
        roomsCombo.setPreferredSize(new Dimension(300, 30));
        roomsCombo.setFont(Constants.FONT);

        inputPanel.add(checkInLabel);
        inputPanel.add(checkInField, "wrap");
        inputPanel.add(checkOutLabel);
        inputPanel.add(checkOutField, "wrap");
        inputPanel.add(roomsLabel);
        inputPanel.add(roomsCombo, "wrap");
        inputPanel.add(guestsLabel);
        inputPanel.add(guestsCombo);
        inputPanel.add(addGuestButton, "span, split 2");
        inputPanel.add(removeGuestButton, "wrap");
        inputPanel.add(new JScrollPane(guestsTable), "grow, span, wrap");

        return inputPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Sans", Font.BOLD, 20));
        return label;
    }

    private void setInitialDates(Reservation entity) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getInitialDate())) {
            checkInField.setText(formatDate(convertToLocalDateTimeViaInstant(entity.getInitialDate())));
        } else {
            checkInField.setText(formatDate(today.atStartOfDay()));
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getFinalDate())) {
            checkOutField.setText(formatDate(convertToLocalDateTimeViaInstant(entity.getFinalDate())));
        } else {
            checkOutField.setText(formatDate(tomorrow.atStartOfDay()));
        }
    }

    private JTable createGuestsTable() {
        JTable guestsTable = new JTable();
        guestsTable.setModel(model);
        guestsTable.setFont(new Font("Sans", Font.PLAIN, 20));
        guestsTable.setSelectionForeground(Color.WHITE);
        guestsTable.setAutoCreateRowSorter(true);
        guestsTable.setRowHeight(30);
        guestsTable.setDefaultEditor(Object.class, null);
        return guestsTable;
    }

    private JButton createAddGuestButton() {
        JButton addGuestButton = new JButton("Adicionar hóspede");
        addGuestButton.addActionListener(e -> {
            final Guest selectedGuest = guests.get(guestsCombo.getSelectedIndex());
            if (!guestsTableList.contains(selectedGuest)) {
                guestsTableList.add(selectedGuest);
                updateGuestsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Hóspede já adicionado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        return addGuestButton;
    }

    private JButton createRemoveGuestButton(JTable guestsTable) {
        JButton removeGuestButton = new JButton("Remover hóspede");
        removeGuestButton.addActionListener(e -> {
            final int selectedRow = guestsTable.getSelectedRow();
            if (selectedRow != -1) {
                guestsTableList.remove(selectedRow);
                updateGuestsTable();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um hóspede para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        return removeGuestButton;
    }

    private JButton createSaveButton(Role role, Reservation entity) {
        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveReservation(role, entity));
        return saveButton;
    }

    private JButton createCancelButton(Role role) {
        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new ReservationView(role);
            dispose();
        });
        return cancelButton;
    }

    private void addDateFieldListeners() {
        DocumentListener dateFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRoomsCombo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRoomsCombo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRoomsCombo();
            }
        };
        checkInField.getDocument().addDocumentListener(dateFieldListener);
        checkOutField.getDocument().addDocumentListener(dateFieldListener);
    }

    private void updateRoomsCombo() {
        if (isDateFieldValid(checkInField) && isDateFieldValid(checkOutField)) {
            try {
                LocalDate checkInDate = LocalDate.parse(checkInField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate checkOutDate = LocalDate.parse(checkOutField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                populateRoomsCombo(convertToDateViaInstant(checkInDate.atStartOfDay()), convertToDateViaInstant(checkOutDate.atStartOfDay()));
            } catch (Exception ex) {
                // Silenciar exceção para evitar mensagens repetidas desnecessárias
            }
        }
    }

    private void saveReservation(Role role, Reservation entity) {
        try {
            LocalDate checkInDate = LocalDate.parse(checkInField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate checkOutDate = LocalDate.parse(checkOutField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            final Reservation reservation = new Reservation();
            reservation.setIdReservation(Objects.nonNull(entity) ? entity.getIdReservation() : null);
            reservation.setInitialDate(convertToDateViaInstant(checkInDate.atStartOfDay()));
            reservation.setFinalDate(convertToDateViaInstant(checkOutDate.atStartOfDay()));
            reservation.setBranch(roomsCombo.getItemCount() > 0 ? rooms.get(roomsCombo.getSelectedIndex()).getBranch() : null);
            reservation.setRoom(roomsCombo.getItemCount() > 0 ? rooms.get(roomsCombo.getSelectedIndex()) : null);
            reservation.setUser(Objects.nonNull(entity) ? entity.getUser() : role.getUser());
            reservation.setStatus(Objects.nonNull(entity) ? entity.getStatus() : "P");
            reservation.setGuestList(guestsTableList);

            final Boolean result = Objects.nonNull(entity) ? ReservationService.update(reservation) : ReservationService.save(reservation);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new ReservationView(role);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datas de entrada/saída inválidas!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateGuestsTable() {
        model.setRowCount(0);
        for (Guest guest : guestsTableList) {
            model.addRow(new Object[]{
                    guest.getName(),
                    guest.getCpf() != null ? Util.maskCpf(guest.getCpf()) : "-",
                    guest.getPassportNumber() != null ? guest.getPassportNumber() : "-"
            });
        }
    }

    private void loadReservationGuests(Reservation reservation) {
        Reservation fullReservation = ReservationService.findByIdWithGuests(reservation.getIdReservation());
        List<Guest> reservationGuests = fullReservation.getGuestList();
        guestsTableList.addAll(reservationGuests);
        updateGuestsTable();
    }

    private void populateGuestCombo(Role role) {
        final List<Guest> guestList = GuestService.findAll(role);

        if (Objects.nonNull(guestList) && !guestList.isEmpty()) {
            guestsCombo.removeAllItems();
            guests.clear();

            for (Guest guest : guestList) {
                guestsCombo.addItem(guest.getName() + " - " + (Objects.isNull(guest.getCpf()) ? guest.getPassportNumber() : Util.maskCpf(guest.getCpf())));
                guests.add(guest);
            }
        }
    }

    private void populateRoomsCombo(Date checkInDate, Date checkOutDate) {
        final List<Room> roomList = RoomService.findAvailableRooms(checkInDate, checkOutDate);

        if (Objects.nonNull(roomList) && !roomList.isEmpty()) {
            roomsCombo.removeAllItems();
            rooms.clear();

            for (Room room : roomList) {
                roomsCombo.addItem(room.getRoomNumber().toString() + " - " + room.getBranch().getName());
                rooms.add(room);
            }
        } else {
            System.out.println("Nenhum quarto disponível.");
        }
    }

    private JFormattedTextField createFormattedDateField() {
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            return new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        if (dateToConvert instanceof java.sql.Date) {
            return ((java.sql.Date) dateToConvert).toLocalDate().atStartOfDay();
        } else {
            return dateToConvert.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    private boolean isDateFieldValid(JFormattedTextField dateField) {
        return dateField != null && dateField.getText() != null && !dateField.getText().contains("_");
    }
}
