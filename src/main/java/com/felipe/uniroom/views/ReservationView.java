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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.felipe.uniroom.config.Util.formatNullableDate;

public class ReservationView extends JFrame {
    private static DefaultTableModel model;
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

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Quarto", "Funcionário", "Filial", "Data inicial", "Data final", "Check-In", "Check-out", "Situação"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final TableColumn optionsColumn = table.getColumnModel().getColumn(0);
        optionsColumn.setCellRenderer(new Components.OptionsCellRenderer());

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final Reservation reservation = ReservationService.findById((Integer) model.getValueAt(row, 1));

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
                        try {
                            updateReservationTable(role);
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Components.showGenericError(this);
                    }
                });

                final JMenuItem checkoutItem = new JMenuItem(Constants.CHECKOUT);
                checkoutItem.setIcon(Constants.CORPORATE_ICON);
                checkoutItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                checkoutItem.addActionListener(e -> {
                    if (ReservationService.checkout(reservation)) {
                        try {
                            updateReservationTable(role);
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Components.showGenericError(this);
                    }
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
                        try {
                            updateReservationTable(role);
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Components.showGenericError(this);
                    }
                });

                popupMenu.add(expenseItem);
                if (!reservation.getStatus().equals("C") && !reservation.getStatus().equals("CO")) {
                    popupMenu.add(editItem);
                    popupMenu.add(deleteItem);
                }

                popupMenu.show(tableEvt, evt.getX(), evt.getY());
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        try {
            updateReservationTable(role);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateReservationTable(Role role) throws ParseException {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Reservation reservation : searchItens) {
                model.addRow(new Object[]{null, reservation.getIdReservation(), reservation.getRoom().getRoomNumber(), reservation.getUser().getName(), reservation.getBranch().getName(), formatNullableDate(reservation.getInitialDate(), "dd/MM/yyyy"), formatNullableDate(reservation.getFinalDate(), "dd/MM/yyyy"), formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm"), formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm"), Util.convertStatusReservation(reservation.getStatus())});
            }
            searchItens.clear();
        } else {
            final List<Reservation> reservationList = ReservationService.findAll(role);
            if (Objects.nonNull(reservationList)) {
                for (Reservation reservation : reservationList) {
                    model.addRow(new Object[]{null, reservation.getIdReservation(), reservation.getRoom().getRoomNumber(), reservation.getUser().getName(), reservation.getBranch().getName(), formatNullableDate(reservation.getInitialDate(), "dd/MM/yyyy"), formatNullableDate(reservation.getFinalDate(), "dd/MM/yyyy"), formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm"), formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm"), Util.convertStatusReservation(reservation.getStatus())});
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }
}
