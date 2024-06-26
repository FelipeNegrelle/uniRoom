package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Expense;
import com.felipe.uniroom.entities.Reservation;
import com.felipe.uniroom.services.ExpenseService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpenseView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Expense> searchItens = new ArrayList<>();

    public ExpenseView(Role role, Reservation reservation) {
        super(Constants.EXPENSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.EXPENSE);
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
            new ReservationView(role);
            dispose();
        });
        searchPanel.add(backButton, "align left");

        final JButton newExpense = new JButton(Constants.NEW);
        newExpense.setBackground(Constants.WHITE);
        newExpense.setForeground(Constants.BLACK);
        newExpense.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newExpense.addActionListener(e -> {
            new ExpenseForm(role, reservation, null);
            dispose();
        });
        if (!reservation.getStatus().equals("C") && !reservation.getStatus().equals("CO")) {
//            newExpense.setEnabled(false);
            searchPanel.add(newExpense, "align left");
        }

        final JLabel searchLabel = new JLabel(Constants.SEARCH + ":");
        searchLabel.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchLabel.setForeground(Constants.WHITE);
        searchPanel.add(searchLabel, "align right");
        final JTextField searchField = new JTextField(20);
        searchField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchItens.addAll(ExpenseService.search(searchField.getText(), null, role));
                updateExpenseTable(reservation ,role);
            }
        });
        searchPanel.add(searchField, "align right");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Item", "Serviço", "Quantidade",Constants.GUEST , "Data/Hora despesa"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final TableColumn optionsColumn = table.getColumnModel().getColumn(0);
        optionsColumn.setCellRenderer(new Components.OptionsCellRenderer());

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Expense expense = ExpenseService.findById((int) model.getValueAt(row, 1));

                    new ExpenseForm(role, reservation, expense);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Expense expense = new Expense();

                    expense.setIdExpense((int) model.getValueAt(row, 1));

                    if (ExpenseService.delete(expense)) {
                        updateExpenseTable(reservation ,role);
                    } else {
                        Components.showGenericError(this);
                    }
                });

                if (!reservation.getStatus().equals("C") && !reservation.getStatus().equals("CO")) {
                    popupMenu.add(editItem);
                    popupMenu.add(deleteItem);

                    popupMenu.show(tableEvt, evt.getX(), evt.getY());
                }
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        updateExpenseTable(reservation ,role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateExpenseTable(Reservation reservation,  Role role) {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Expense expense : searchItens) {
                model.addRow(new Object[]{
                        null,
                        expense.getIdExpense(),
                        Objects.isNull(expense.getItem()) ? "-" : expense.getItem().getName(),
                        Objects.isNull(expense.getService()) ? "-" : expense.getService().getDescription(),
                        expense.getAmount(),
                        expense.getGuest().getName(),
                        Util.formatNullableDate(expense.getDateTimeExpense(), "dd/MM/yyyy HH:mm"),
                });
            }
            searchItens.clear();
        } else {
            final List<Expense> expenseList = ExpenseService.findByReservationId(reservation.getIdReservation());
            if (Objects.nonNull(expenseList)) {
                for (Expense expense : expenseList) {
                    model.addRow(new Object[]{
                            null,
                            expense.getIdExpense(),
                            Objects.isNull(expense.getItem()) ? "-" : expense.getItem().getName(),
                            Objects.isNull(expense.getService()) ? "-" : expense.getService().getDescription(),
                            expense.getAmount(),
                            expense.getGuest().getName(),
                            Util.formatNullableDate(expense.getDateTimeExpense(), "dd/MM/yyyy HH:mm"),
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }
}
