package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.felipe.uniroom.config.Util.formatNullableDate;

public class ReservationView extends JFrame {
    private static final DefaultTableModel model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", Constants.ROOM, "Funcionário", Constants.BRANCH, "Data inicial", "Data final", Constants.CHECKIN, Constants.CHECKOUT, Constants.STATUS}, 0);

    private static final List<Reservation> searchItens = new ArrayList<>();

    public ReservationView(Role role) {
        super(Constants.RESERVATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.RESERVATION);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, "align center");

        final JPanel searchPanel = new JPanel(new MigLayout("insets 0", "[right]unrel[grow]unrel[]", "[grow]"));
        searchPanel.setBackground(Constants.BLUE);

        final JButton backButton = new JButton(Constants.BACK);
        backButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
        backButton.setBackground(Constants.WHITE);
        backButton.setIcon(Constants.BACK_ICON);
        backButton.addActionListener(e -> {
            new Home(role);
            dispose();
        });
        searchPanel.add(backButton, "align left ");

        final JButton newReservation = new JButton(Constants.NEW);
        newReservation.setBackground(Constants.WHITE);
        newReservation.setForeground(Constants.BLACK);
        newReservation.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newReservation.addActionListener(e -> {
            new ReservationForm(role, null);
            dispose();
        });
        searchPanel.add(newReservation, "align left");

        final JTable table = new JTable(model);

        final JLabel initialDateLabel = Components.getLabel("Data inicial: ", null, Font.BOLD, 20, Constants.WHITE);
        searchPanel.add(initialDateLabel, "align left");

        final JFormattedTextField initialDateField = Util.createFormattedDateField();
        initialDateField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        initialDateField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(initialDateField, "align right");

        final JLabel finalDateLabel = Components.getLabel("Data final: ", null, Font.BOLD, 20, Constants.WHITE);
        searchPanel.add(finalDateLabel, "align left");

        final JTextField finalDateField = Util.createFormattedDateField();
        finalDateField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        finalDateField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(finalDateField, "align right");

        final JButton filterButton = new JButton("Filtrar");
        filterButton.setBackground(Constants.WHITE);
        filterButton.setForeground(Constants.BLACK);
        filterButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
        filterButton.addActionListener(e -> {
            final String initialDate = initialDateField.getText().trim();
            final String finalDate = finalDateField.getText().trim();

            if (initialDate.isEmpty() || finalDate.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                searchItens.clear();
                searchItens.addAll(getByDate(role, initialDate, finalDate));
                updateReservationTable(role);
            }
        });
        searchPanel.add(filterButton, "align right");

        final JButton clearFilter = new JButton("Limpar filtro");
        clearFilter.setBackground(Constants.WHITE);
        clearFilter.setForeground(Constants.BLACK);
        clearFilter.setFont(Constants.FONT.deriveFont(Font.BOLD));
        clearFilter.addActionListener(e -> {
            initialDateField.setText("");
            finalDateField.setText("");
            searchItens.clear();
            updateReservationTable(role);
        });
        searchPanel.add(clearFilter, "align right");

        panel.add(searchPanel, "growx");

        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        table.getColumnModel().getColumn(9).setCellRenderer(new Components.StatusCellRenderer());

        final TableColumn optionsColumn = table.getColumnModel().getColumn(0);
        optionsColumn.setCellRenderer(new Components.OptionsCellRenderer());

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final Reservation reservation = ReservationService.findById((Integer) model.getValueAt(row, 1));

                final JMenuItem viewItem = new JMenuItem(Constants.RESUME);
                viewItem.setIcon(Constants.DASHBOARD_ICON);
                viewItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                viewItem.addActionListener(e -> {
                    new ReservationResume(role, reservation);
                    dispose();
                });

                final JMenuItem expenseItem = new JMenuItem(Constants.EXPENSE);
                expenseItem.setIcon(Constants.EXPENSE_ICON);
                expenseItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                expenseItem.addActionListener(e -> {
                    new ExpenseView(role, reservation);
                    dispose();
                });

                final JMenuItem checkinItem = new JMenuItem(Constants.CHECKIN);
                checkinItem.setIcon(Constants.CORPORATE_ICON);
                checkinItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                checkinItem.addActionListener(e -> {
                    if (ReservationService.checkin(reservation)) {
                        updateReservationTable(role);
                    }
                });

                final JMenuItem checkoutItem = new JMenuItem(Constants.CHECKOUT);
                checkoutItem.setIcon(Constants.CORPORATE_ICON);
                checkoutItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                checkoutItem.addActionListener(e -> {
                    new ReservationCheckoutForm(reservation, role);
                    dispose();
                });

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    new ReservationForm(role, reservation);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.CANCEL);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    if (ReservationService.cancel(reservation)) {
                        updateReservationTable(role);
                    }
                });

                if (!reservation.getStatus().equals("C")) {
                    popupMenu.add(viewItem);
                    popupMenu.add(expenseItem);
                    if (reservation.getDateTimeCheckIn() == null) {
                        popupMenu.add(checkinItem);
                    }

                    if (reservation.getDateTimeCheckIn() != null && reservation.getDateTimeCheckOut() == null) {
                        popupMenu.add(checkoutItem);
                    }

                    if (!reservation.getStatus().equals("CO")) {
                        popupMenu.add(editItem);
                        popupMenu.add(deleteItem);
                    }
                }

                popupMenu.show(tableEvt, evt.getX(), evt.getY());
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        updateReservationTable(role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateReservationTable(Role role) {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Reservation reservation : searchItens) {
                model.addRow(new Object[]{
                        null,
                        reservation.getIdReservation(),
                        reservation.getRoom().getRoomNumber(),
                        reservation.getUser().getName(),
                        reservation.getBranch().getName(),
                        formatNullableDate(reservation.getInitialDate(), "dd/MM/yyyy"),
                        formatNullableDate(reservation.getFinalDate(), "dd/MM/yyyy"),
                        formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm"),
                        formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm"),
                        Util.convertStatusReservation(reservation.getStatus())
                });
            }

            searchItens.clear();
        } else {
            final List<Reservation> reservationList = ReservationService.findAll(role);

            if (Objects.nonNull(reservationList)) {
                for (Reservation reservation : reservationList) {
                    model.addRow(new Object[]{
                            null,
                            reservation.getIdReservation(),
                            reservation.getRoom().getRoomNumber(),
                            reservation.getUser().getName(),
                            reservation.getBranch().getName(),
                            formatNullableDate(reservation.getInitialDate(), "dd/MM/yyyy"),
                            formatNullableDate(reservation.getFinalDate(), "dd/MM/yyyy"),
                            formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm"),
                            formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm"),
                            Util.convertStatusReservation(reservation.getStatus())
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }

    private static List<Reservation> getByDate(Role role, String initialDate, String finalDate) {
        LocalDate checkInDate = LocalDate.parse(initialDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate checkOutDate = LocalDate.parse(finalDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        final List<Reservation> reservationList = ReservationService.findAll(role);

        return reservationList.stream()
                .filter(reservation -> {
                    LocalDate reservationCheckIn = Util.getLocalDate(reservation.getInitialDate());
                    LocalDate reservationCheckOut = Util.getLocalDate(reservation.getFinalDate());
                    return (!reservationCheckIn.isBefore(checkInDate) && !reservationCheckIn.isAfter(checkOutDate))
                            || (!reservationCheckOut.isBefore(checkInDate) && !reservationCheckOut.isAfter(checkOutDate))
                            || (!reservationCheckIn.isAfter(checkOutDate) && !reservationCheckOut.isBefore(checkInDate));
                })
                .collect(Collectors.toList());
    }

}