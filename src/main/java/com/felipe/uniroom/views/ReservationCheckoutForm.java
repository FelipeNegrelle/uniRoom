package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.ReservationMovement;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ReservationCheckoutForm extends JFrame {
    private static DefaultTableModel model;
    private static JLabel totalDaysLabel;
    private static JLabel totalExpensesLabel;
    private static JLabel grandTotalLabel;
    private static final List<ReservationMovement> expenses = new ArrayList<>();

    ReservationCheckoutForm(Reservation reservation, Role role) {
        super("Check-Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill", "[grow]", "[][grow][]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JLabel titleLabel = new JLabel("Check-Out");
        titleLabel.setFont(new Font("Sans", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        final JPanel mainPanel = new JPanel(new MigLayout("fill", "[grow 70][grow 30]", "[grow]"));
        mainPanel.setBackground(Color.WHITE);

        final JPanel tablePanel = new JPanel(new MigLayout("fill"));
        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(new Object[]{Constants.NAME, Constants.CPF, Constants.ITEM + " / " + Constants.SERVICE, Constants.QUANTITY, "Pagar"}, 0);

        final JTable guestsTable = new JTable(model);
        guestsTable.setFont(new Font("Sans", Font.PLAIN, 20));
        guestsTable.setSelectionForeground(Color.WHITE);
        guestsTable.setAutoCreateRowSorter(true);
        guestsTable.setRowHeight(30);
        guestsTable.setDefaultEditor(Object.class, null);
        guestsTable.getTableHeader().setReorderingAllowed(false);
        guestsTable.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        guestsTable.getColumn("Pagar").setCellRenderer(new ButtonRenderer());
        guestsTable.getColumn("Pagar").setCellEditor(new ButtonEditor(new JCheckBox("Pagar")));

        final JScrollPane tableScrollPane = new JScrollPane(guestsTable);
        tablePanel.add(tableScrollPane, "grow");

        final JPanel totalsPanel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", "[]20[]20[]20[]20[]"));
        totalsPanel.setBackground(Constants.WHITE);

        totalDaysLabel = new JLabel("Total das Diárias: R$ 0,00");
        totalDaysLabel.setFont(new Font("Sans", Font.BOLD, 20));

        totalExpensesLabel = new JLabel("Total das Despesas: R$ 0,00");
        totalExpensesLabel.setFont(new Font("Sans", Font.BOLD, 20));

        grandTotalLabel = new JLabel("Preço Total: R$ 0,00");
        grandTotalLabel.setFont(new Font("Sans", Font.BOLD, 20));

        totalsPanel.add(totalDaysLabel, "align left");
        totalsPanel.add(totalExpensesLabel, "align left");
        totalsPanel.add(grandTotalLabel, "align left");

        final JPanel buttonPanel = new JPanel(new MigLayout("fillx", "[][]"));
        buttonPanel.setBackground(Color.WHITE);

        final JButton backButton = new JButton("Voltar");
        backButton.setFont(new Font("Sans", Font.BOLD, 20));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setBackground(Constants.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new ReservationView(role);
            dispose();
        });

        JButton checkOutButton = new JButton("Check-Out");
        checkOutButton.setFont(new Font("Sans", Font.BOLD, 20));
        checkOutButton.setPreferredSize(new Dimension(150, 40));
        checkOutButton.setBackground(Constants.BLUE);
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.addActionListener(e -> {
            if (ReservationService.checkout(reservation)) {
                JOptionPane.showMessageDialog(this, "Check-out realizado");

                new ReservationView(role);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao fazer check-out");
            }

        });

        buttonPanel.add(backButton, "align left");
        buttonPanel.add(checkOutButton, "align right");

        totalsPanel.add(buttonPanel, "span, growx");

        mainPanel.add(tablePanel, "grow");
        mainPanel.add(totalsPanel, "grow");

        add(mainPanel, "grow, wrap");

        populateExpenseTable(reservation, role);
        updateTotalDays(reservation, role);
        updateTotalExpenses(reservation, role);
//        updateTotal();

        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        super("Finalizar reserva");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
//        getContentPane().setBackground(Constants.BLUE);
//        setIconImage(Constants.LOGO);
//
//        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center][grow]"));
//
//        final JLabel titleLabel = new JLabel("Finalizar reserva");
//        titleLabel.setFont(Constants.FONT.deriveFont(40f));
//        titleLabel.setForeground(Constants.WHITE);
//        mainPanel.add(titleLabel, "align center, wrap");
//
//        final JPanel subPanel = new JPanel(new MigLayout("fill, insets 10", "[grow][grow]", "[align center]"));
//        subPanel.setBackground(Constants.BLUE);
//
//        model = new DefaultTableModel(new Object[]{Constants.NAME, Constants.CPF, Constants.ITEM + " / " + Constants.SERVICE, Constants.QUANTITY, "Pagar"}, 0);
//
//        final JTable table = new JTable(model);
//        table.setFont(new Font("Sans", Font.PLAIN, 20));
//        table.setSelectionForeground(Color.WHITE);
//        table.setAutoCreateRowSorter(true);
//        table.setRowHeight(30);
//        table.setDefaultEditor(Object.class, null);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
//        subPanel.add(new JScrollPane(table), "span, grow");
//
//        final JPanel valuesPanel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[][][][]"));
//        valuesPanel.setBackground(Constants.BLUE);
//
//        final JLabel daysLabel = new JLabel("Diárias: " + 0);
//        daysLabel.setFont(Constants.FONT.deriveFont(20f));
//        daysLabel.setForeground(Constants.WHITE);
//        valuesPanel.add(daysLabel, "wrap");
//
//        final JLabel expenseLabel = new JLabel("Despesas: R$ " + 0);
//        expenseLabel.setFont(Constants.FONT.deriveFont(20f));
//        expenseLabel.setForeground(Constants.WHITE);
//        valuesPanel.add(expenseLabel, "wrap");
//
//        final JLabel totalLabel = new JLabel("Total: R$ " + 0);
//        totalLabel.setFont(Constants.FONT.deriveFont(20f));
//        totalLabel.setForeground(Constants.WHITE);
//        valuesPanel.add(totalLabel, "wrap");
//
//        final JButton backButton = new JButton(Constants.BACK);
//        backButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
//        backButton.setBackground(Constants.WHITE);
//        backButton.setIcon(Constants.BACK_ICON);
//        backButton.addActionListener(e -> {
//            new ReservationView(role);
//            dispose();
//        });
//        valuesPanel.add(backButton, "align left");
//
//        final JButton checkoutButton = new JButton("Finalizar reserva");
//        checkoutButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
//        checkoutButton.setBackground(Constants.WHITE);
//        checkoutButton.addActionListener(e -> {
////            reservation.setActive(false);
//            new ReservationView(role);
//            dispose();
//        });
//        valuesPanel.add(checkoutButton, "align right");
//
//        subPanel.add(valuesPanel, "grow");
//
//        mainPanel.add(subPanel, "grow");
//
//        add(mainPanel);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setLocationRelativeTo(null);
//        setVisible(true);
    }

    private static void updateTotalDays(Reservation reservation, Role role) {
        final Float result = ReservationService.getTotalDays(reservation, new Date(), role);

        if (Objects.nonNull(result)) {
            totalDaysLabel.setText("Total das Diárias: R$ " + result);
        }
    }

    private static void populateExpenseTable(Reservation reservation, Role role) {
        final List<Expense> expenses = ReservationService.getExpensesReservation(reservation, role);

        if (Objects.nonNull(expenses)) {
            for (Expense expense : expenses) {
                model.addRow(new Object[]{
                        expense.getGuest().getName(),
                        expense.getGuest().getCpf(),
                        expense.getItem() != null ? expense.getItem().getName() : expense.getService().getDescription(),
                        expense.getAmount(),
                        null
                });
            }
        }
    }

    private static void updateTotalExpenses(Reservation reservation, Role role) {
//        final Float result = ReservationService.getTotalExpenses(reservation, role);
        float result = 0f;
        if (Objects.nonNull(result)) {
            totalExpensesLabel.setText("Total das Despesas: R$ " + result);
        }
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Pagar");
//            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(button, "Pagamento realizado para " + label);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
