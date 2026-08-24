package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.entities.ReservationMovement;
import com.felipe.uniroom.services.ReservationMovementService;
import com.felipe.uniroom.services.ReservationService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ReservationCheckoutForm extends JFrame {
    private static ExpenseTableModel model;
    private static JLabel totalDaysLabel;
    private static JLabel totalExpensesLabel;
    private static JLabel grandTotalLabel;
    private static final List<ReservationMovement> movements = new ArrayList<>();
    private static final Map<Integer, Expense> expenses = new HashMap<>();

    ReservationCheckoutForm(Reservation reservation, Role role) {
        super("Check-Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill", "[grow]", "[][grow][]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JLabel titleLabel = new JLabel(Constants.CHECKOUT);
        titleLabel.setFont(new Font("Sans", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, "align center, wrap");

        final JPanel mainPanel = new JPanel(new MigLayout("fill", "[grow 70][grow 30]", "[grow]"));
        mainPanel.setBackground(Color.WHITE);

        final JPanel tablePanel = new JPanel(new MigLayout("fill"));
        tablePanel.setBackground(Color.WHITE);

        model = new ExpenseTableModel();

        final JTable guestsTable = new JTable(model);
        guestsTable.setFont(new Font("Sans", Font.PLAIN, 20));
        guestsTable.setSelectionForeground(Color.WHITE);
        guestsTable.setAutoCreateRowSorter(true);
        guestsTable.setRowHeight(30);
        guestsTable.setDefaultEditor(Object.class, null);
        guestsTable.getTableHeader().setReorderingAllowed(false);
        guestsTable.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        guestsTable.getColumn("Pagar").setCellRenderer(new ButtonRenderer());
        guestsTable.getColumn("Pagar").setCellEditor(new ButtonEditor(role));

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

        final JButton backButton = new JButton(Constants.BACK);
        backButton.setFont(new Font("Sans", Font.BOLD, 20));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setBackground(Constants.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new ReservationView(role);
            dispose();
        });

        JButton checkOutButton = new JButton(Constants.CHECKOUT);
        checkOutButton.setFont(new Font("Sans", Font.BOLD, 20));
        checkOutButton.setPreferredSize(new Dimension(150, 40));
        checkOutButton.setBackground(Constants.BLUE);
        checkOutButton.setForeground(Color.WHITE);
        checkOutButton.addActionListener(e -> {
            final ReservationMovement stays = new ReservationMovement();
            stays.setReservation(reservation);
            stays.setExpense(null);
            stays.setMovementType("I");
            stays.setDescription("Pagamento da reserva");
            stays.setValue(ReservationService.getTotalDays(reservation, new Date(), role));
            stays.setDateTimeMovement(new Date());
            movements.add(stays);

            boolean saved = true;
            for (ReservationMovement movement : movements) {
                saved = ReservationMovementService.save(movement, role);

                if (!saved) {
                    break;
                }
            }

            if (saved) {
                if (ReservationService.checkout(reservation)) {
                    JOptionPane.showMessageDialog(this, "Check-out realizado");

                    new ReservationView(role);

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao fazer check-out");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar despesas");
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
        updateTotalExpenses(reservation);
        updateTotal(reservation, role);

        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void updateTotalDays(Reservation reservation, Role role) {
        final Double result = ReservationService.getTotalDays(reservation, new Date(), role);

        if (Objects.nonNull(result)) {
            totalDaysLabel.setText("Total das Diárias: R$ " + String.format("%.2f", result));
        }
    }

    private static void populateExpenseTable(Reservation reservation, Role role) {
        final List<Expense> expenseList = ReservationService.getExpensesReservation(reservation, role);

        if (Objects.nonNull(expenseList)) {
            for (Expense expense : expenseList) {
                expenses.put(expense.getIdExpense(), expense);

                model.addExpense(expense);
            }
        }
    }

    private static void updateTotalExpenses(Reservation reservation) {
        final Double result = ReservationService.getTotalExpensesReservation(reservation.getIdReservation());

        totalExpensesLabel.setText("Total das Despesas: R$ " + String.format("%.2f", result));
    }

    private static void updateTotal(Reservation reservation, Role role) {
        final Double daily = ReservationService.getTotalDays(reservation, new Date(), role);

        final Double expenses = ReservationService.getTotalExpensesReservation(reservation.getIdReservation());

        final Double result = daily + expenses;

        grandTotalLabel.setText("Preço total: R$ " + String.format("%.2f", result));
    }

    private static final class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "Pagar");
            setBackground(value != null && value.toString().equals("Estornar") ? Constants.RED : Constants.GREEN);
            setForeground(Constants.WHITE);

            return this;
        }
    }

    private static final class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int row;

        public ButtonEditor(Role role) {
            super(new JCheckBox());
            button = new JButton("Pagar");
            button.setOpaque(true);
            button.addActionListener(e -> {
                togglePaymentState(row, role);
                fireEditingStopped(); // Para terminar a edição após o clique
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;

            button.setText(value != null ? value.toString() : "Pagar");
            button.setBackground(value != null && value.toString().equals("Estornar") ? Constants.RED : Constants.GREEN);
            button.setForeground(Constants.WHITE);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        private void togglePaymentState(int row, Role role) {
            int expenseId = (int) model.getValueAt(row, 0);

            final Expense ex = expenses.get(expenseId);

            if (model.getState(row).equals('P')) {
                final StringBuilder expenseDesc = new StringBuilder();

                double value;

                expenseDesc.append("Pagamento de ").append(ex.getGuest().getName()).append(" do ");
                if (Objects.nonNull(ex.getItem())) {
                    expenseDesc.append("item ").append(ex.getItem().getName());
                    value = ex.getItem().getPrice() * ex.getAmount();
                } else {
                    expenseDesc.append("serviço ").append(ex.getService().getDescription());
                    value = ex.getService().getPrice() * ex.getAmount();
                }

                final ReservationMovement expense = new ReservationMovement();
                expense.setExpense(ex);
                expense.setReservation(ex.getReservation());
                expense.setDateTimeMovement(new Date());
                expense.setMovementType("I");
                expense.setDescription(expenseDesc.toString());
                expense.setValue(value);
                movements.add(expense);

                model.setState(row, 'E');
            } else {
                movements.removeIf(movement -> movement.getExpense().getIdExpense() == expenseId);

                model.setState(row, 'P');
            }

            updateTotalExpenses(ex.getReservation());
            updateTotal(ex.getReservation(), role);
        }
    }

    private static class ExpenseTableModel extends AbstractTableModel {
        private final List<Expense> expenses = new ArrayList<>();
        private final List<Character> states = new ArrayList<>();
        private final String[] columnNames = {"Código", Constants.NAME, Constants.CPF, Constants.ITEM + " / " + Constants.SERVICE, Constants.QUANTITY, "Valor total", "Pagar"};

        public void addExpense(Expense expense) {
            expenses.add(expense);
            states.add('P');
            fireTableRowsInserted(expenses.size() - 1, expenses.size() - 1);
        }

        public Character getState(int row) {
            return states.get(row);
        }

        public void setState(int row, Character state) {
            states.set(row, state);
            fireTableCellUpdated(row, 6);
        }

        @Override
        public int getRowCount() {
            return expenses.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Expense expense = expenses.get(rowIndex);
            return switch (columnIndex) {
                case 0 ->
                        expense.getIdExpense();
                case 1 ->
                        expense.getGuest().getName();
                case 2 ->
                        Util.maskCpf(expense.getGuest().getCpf());
                case 3 ->
                        expense.getItem() != null ? expense.getItem().getName() : expense.getService().getDescription();
                case 4 ->
                        expense.getAmount();
                case 5 ->
                        expense.getItem() != null ? expense.getItem().getPrice() * expense.getAmount() : expense.getService().getPrice() * expense.getAmount();
                case 6 ->
                        states.get(rowIndex) == 'P' ? "Pagar" : "Estornar";
                default ->
                        null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 6;
        }
    }
}
