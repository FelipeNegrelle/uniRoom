package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReservationResume extends JFrame {
    private static DefaultTableModel guestsModel;
    private static DefaultTableModel expensesModel;

    private static Integer daysHosted;
    private static Double totalDays;
    private static Double totalExpenses;

    public ReservationResume(Role role, Reservation reservation) {
        super("Resumo da reserva");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill", "[grow]", "[][grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        // Título no topo
        final JLabel titleLabel = new JLabel(Constants.RESERVATION + " " + Constants.RESUME);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        // Painel principal com gap de 10 entre os painéis
        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 0 10 10 10", "[grow 80, fill]20[grow 20, fill]", "[grow]"));
        mainPanel.setBackground(Constants.BLUE);

        // Painel esquerdo (hóspedes e despesas)
        final JPanel tablesPanel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", "[][grow, fill][][grow, fill]"));
        tablesPanel.setBackground(Color.WHITE);

        // Tabela de hóspedes
        final JLabel guestsLabel = new JLabel("Hóspedes");
        guestsLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        tablesPanel.add(guestsLabel, "align center");

        guestsModel = new DefaultTableModel(new Object[]{"Nome", "CPF"}, 0);
        final JTable guestsTable = new JTable(guestsModel);
        guestsTable.setFont(Constants.FONT);
        guestsTable.setRowHeight(30);
        guestsTable.getTableHeader().setReorderingAllowed(false);
        guestsTable.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        guestsTable.setEnabled(false);
        tablesPanel.add(new JScrollPane(guestsTable), "grow");

        // Tabela de despesas
        final JLabel expensesLabel = new JLabel("Despesas");
        expensesLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        tablesPanel.add(expensesLabel, "align center");

        expensesModel = new DefaultTableModel(new Object[]{"Hóspede", "Item/Serviço", "Quantidade", "Valor"}, 0);
        final JTable expensesTable = new JTable(expensesModel);
        expensesTable.setFont(Constants.FONT);
        expensesTable.setRowHeight(30);
        expensesTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        expensesTable.getTableHeader().setReorderingAllowed(false);
        expensesTable.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        expensesTable.setEnabled(false);
        tablesPanel.add(new JScrollPane(expensesTable), "grow");

        mainPanel.add(tablesPanel, "grow");

        final JPanel infosPanel = new JPanel(new MigLayout("insets 0 30 0 30, fill", "[grow][grow]", "[grow]20[grow]20[grow]20[grow]20[grow]20[grow]20[grow]push[]20"));
        infosPanel.setBackground(Color.WHITE);

        final JLabel datesLabel = Components.getLabel(getReservationPeriod(reservation), null, Font.BOLD, null, null);
        final JLabel checkinLabel = Components.getLabel(getCheckIn(reservation), null, Font.BOLD, null, null);
        final JLabel checkoutLabel = Components.getLabel(getCheckOut(reservation), null, Font.BOLD, null, null);
        final JLabel roomBranchLabel = Components.getLabel(getRoom(reservation), null, Font.BOLD, null, null);
        final JLabel statusLabel = Components.getLabel(getStatus(reservation), null, Font.BOLD, null, null);
        final JLabel daysLabel = Components.getLabel(getStaysSinceCheckIn(reservation), null, Font.BOLD, null, null);
        final JLabel totalExpensesLabel = Components.getLabel(getTotalExpenses(reservation), null, Font.BOLD, null, null);
        final JLabel totalStayLabel = Components.getLabel(getTotalStays(reservation), null, Font.BOLD, null, null);
        final JLabel grandTotalLabel = Components.getLabel(getGrandTotal(reservation), null, Font.BOLD, null, null);

        infosPanel.add(datesLabel, "align center, span");
        infosPanel.add(checkinLabel, "align left");
        infosPanel.add(checkoutLabel, "align right, span");
        infosPanel.add(roomBranchLabel, "align left, span");
        infosPanel.add(statusLabel, "align left, span");
        infosPanel.add(daysLabel, "align left, span");
        infosPanel.add(totalExpensesLabel, "align left");
        infosPanel.add(totalStayLabel, "align right, span");
        infosPanel.add(grandTotalLabel, "align center, span");

        final JButton backButton = new JButton(Constants.BACK);
        backButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 30));
        backButton.setPreferredSize(new Dimension(400, 50));
        backButton.setBackground(Constants.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new ReservationView(role);
            dispose();
        });

        infosPanel.add(backButton, "align center, span");

        mainPanel.add(infosPanel, "grow, span");

        add(mainPanel, "grow");

        populateExpensesTable(reservation, role);
        populateGuestTable(reservation);

        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static String getReservationPeriod(Reservation reservation) {
        final String initialPeriod = Util.formatNullableDate(reservation.getInitialDate(), "dd/MM/yyyy");

        final String finalPeriod = Util.formatNullableDate(reservation.getFinalDate(), "dd/MM/yyyy");

        return "Reserva de " + initialPeriod + " até " + finalPeriod;
    }

    private static String getCheckIn(Reservation reservation) {
        String checkIn;

        if (Objects.nonNull(reservation.getDateTimeCheckIn())) {
            checkIn = Util.formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm");
        } else {
            checkIn = " - ";
        }

        return "Data/Hora do Check-In: " + checkIn;
    }

    private static String getCheckOut(Reservation reservation) {
        String checkOut;

        if (Objects.nonNull(reservation.getDateTimeCheckOut())) {
            checkOut = Util.formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm");
        } else {
            checkOut = " - ";
        }

        return "Data/Hora do Check-Out: " + checkOut;
    }

    private static String getRoom(Reservation reservation) {
        final String room = String.valueOf(reservation.getRoom().getRoomNumber());

        final String branch = reservation.getBranch().getName();

        return "Quarto " + room + " do estabelecimento " + branch;
    }

    private static String getStatus(Reservation reservation) {
        final String status = Util.convertStatusReservation(reservation.getStatus());

        return "Status da reserva: " + status;
    }

    private static String getStaysSinceCheckIn(Reservation reservation) {
        if (Objects.nonNull(reservation.getDateTimeCheckIn())) {
            final LocalDate checkIn = Util.getLocalDate(reservation.getDateTimeCheckIn());

            LocalDate finalDate;

            if (Objects.nonNull(reservation.getDateTimeCheckOut())) {
                finalDate = Util.getLocalDate(reservation.getDateTimeCheckOut());
            } else {
                finalDate = Util.getLocalDate(new Date());
            }

            final int days = getPeriod(checkIn, finalDate) + 1;

            return "Diárias: " + days;
        } else {
            return "Diárias: -";
        }
    }

    private static String getTotalExpenses(Reservation reservation) {
        final Double totalExpenses = ReservationService.getTotalExpensesReservation(reservation.getIdReservation());

        return "Total de despesas: R$ " + String.format("%.2f", totalExpenses);
    }

    private static String getTotalStays(Reservation reservation) {
        final float stayPrice = reservation.getRoom().getRoomType().getPrice();

        final LocalDate initialDate = Util.getLocalDate(reservation.getInitialDate());

        LocalDate finalDate;

        if (Objects.nonNull(reservation.getDateTimeCheckOut())) {
            finalDate = Util.getLocalDate(reservation.getDateTimeCheckOut());
        } else {
            finalDate = Util.getLocalDate(new Date());
        }

        final int days = getPeriod(initialDate, finalDate) + 1;

        return "Total de diárias: R$ " + String.format("%.2f", (days * stayPrice));
    }

    private static String getGrandTotal(Reservation reservation) {
        final Double totalExpenses = ReservationService.getTotalExpensesReservation(reservation.getIdReservation());

        final double stayPrice = reservation.getRoom().getRoomType().getPrice();

        final LocalDate initialDate = Util.getLocalDate(reservation.getInitialDate());

        LocalDate finalDate;

        if (Objects.nonNull(reservation.getDateTimeCheckOut())) {
            finalDate = Util.getLocalDate(reservation.getDateTimeCheckOut());
        } else {
            finalDate = Util.getLocalDate(new Date());
        }

        final int days = getPeriod(initialDate, finalDate) + 1;

        final double stayTotal = (days * stayPrice);

        final double grandTotal = stayTotal + totalExpenses;

        return "Valor total da reserva: R$ " + String.format("%.2f", grandTotal);
    }

    private static int getPeriod(LocalDate initialDate, LocalDate finalDate) {
        return Math.toIntExact(ChronoUnit.DAYS.between(initialDate, finalDate));
    }

    private static void populateGuestTable(Reservation reservation) {
        guestsModel.setRowCount(0);

        reservation.getGuestList().forEach(guest -> {
            guestsModel.addRow(new Object[]{
                    guest.getName(),
                    Util.maskCpf(guest.getCpf())
            });
        });
    }

    private static void populateExpensesTable(Reservation reservation, Role role) {
        final List<Expense> expenseList = ReservationService.getExpensesReservation(reservation, role);

        if (Objects.nonNull(expenseList)) {
            expensesModel.setRowCount(0);

            expenseList.forEach(expense -> {
                expensesModel.addRow(new Object[]{
                        expense.getGuest().getName() + " - " + (!expense.getGuest().getIsForeigner() ? Util.maskCpf(expense.getGuest().getCpf()) : expense.getGuest().getPassportNumber()),
                        Objects.nonNull(expense.getItem()) ? expense.getItem().getName() : expense.getService().getDescription(),
                        expense.getAmount(),
                        "R$ " + (Objects.nonNull(expense.getItem()) ? expense.getItem().getPrice() * expense.getAmount() : expense.getService().getPrice() * expense.getAmount()),
                });
            });
        }
    }
}
