package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.*;
import com.felipe.uniroom.services.ExpenseService;
import com.felipe.uniroom.services.GuestService;
import com.felipe.uniroom.services.ItemService;
import com.felipe.uniroom.services.ServiceService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExpenseForm extends JFrame {
    private static final List<Item> items = new ArrayList<>();
    private static final List<Service> services = new ArrayList<>();
    private static final List<Guest> guests = new ArrayList<>();

    public ExpenseForm(Role role, Reservation reservation, Expense entity) {
        super(Constants.EXPENSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.EXPENSE);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Constants.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel typeLabel = Components.getLabel(Constants.TYPE + ": ", null, Font.BOLD, 20, null);

        final JRadioButton itemRadio = new JRadioButton();
        itemRadio.setFont(Constants.FONT);
        itemRadio.setForeground(Color.BLACK);
        itemRadio.setText(Constants.ITEM);

        final JRadioButton serviceRadio = new JRadioButton(Constants.SERVICE);
        serviceRadio.setFont(Constants.FONT);
        serviceRadio.setForeground(Color.BLACK);
        serviceRadio.setText(Constants.SERVICE);

        final JLabel itemLabel = Components.getLabel(Constants.ITEM + ": ", null, Font.BOLD, 20, null);
        final JComboBox<String> itemCombo = new JComboBox<>();
        itemCombo.setPreferredSize(new Dimension(300, 30));
        itemCombo.setFont(Constants.FONT);

        final JLabel serviceLabel = Components.getLabel(Constants.SERVICE + ": ", null, Font.BOLD, 20, null);
        final JComboBox<String> serviceCombo = new JComboBox<>();
        serviceCombo.setPreferredSize(new Dimension(300, 30));
        serviceCombo.setFont(Constants.FONT);

        final JLabel guestLabel = Components.getLabel(Constants.GUEST + ": ", null, Font.BOLD, 20, null);
        final JComboBox<String> guestCombo = new JComboBox<>();
        guestCombo.setPreferredSize(new Dimension(300, 30));
        guestCombo.setFont(Constants.FONT);

        final JLabel amountLabel = Components.getLabel(Constants.QUANTITY + ": ", null, Font.BOLD, 20, null);
        final JTextField amountField = new JTextField(20);
        amountField.setPreferredSize(new Dimension(300, 30));
        amountField.setFont(Constants.FONT);

        itemRadio.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (itemRadio.isSelected()) {
                    itemRadio.setText(Constants.ITEM);
                    itemCombo.setEnabled(true);
                    serviceRadio.setSelected(false);
                    serviceCombo.setEnabled(false);
                    serviceCombo.setSelectedItem(null);
                }
            }
        });

        serviceRadio.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (serviceRadio.isSelected()) {
                    serviceRadio.setText(Constants.SERVICE);
                    serviceCombo.setEnabled(true);
                    itemRadio.setSelected(false);
                    itemCombo.setEnabled(false);
                    itemCombo.setSelectedItem(null);
                }
            }
        });

        itemRadio.setSelected(true);
        itemRadio.setText(Constants.ITEM);
        serviceRadio.setText(Constants.SERVICE);
        itemCombo.setEnabled(true);
        serviceRadio.setSelected(false);
        serviceCombo.setEnabled(false);
        serviceCombo.setSelectedItem(null);

        if (Objects.nonNull(entity)) {
            if (Objects.nonNull(entity.getItem())) {
                itemRadio.setSelected(true);
                itemCombo.setEnabled(true);
                itemCombo.setSelectedItem(entity.getItem().getName());
                serviceCombo.setEnabled(false);
                serviceRadio.setSelected(false);
            } else {
                serviceRadio.setSelected(true);
                serviceCombo.setEnabled(true);
                serviceCombo.setSelectedItem(entity.getService().getDescription());
                itemCombo.setEnabled(false);
                itemRadio.setSelected(false);
            }
            amountField.setText(entity.getAmount().toString());
        }

        inputPanel.add(typeLabel);
        inputPanel.add(itemRadio);
        inputPanel.add(serviceRadio, "wrap");
        inputPanel.add(itemLabel);
        inputPanel.add(itemCombo, "wrap");
        inputPanel.add(serviceLabel);
        inputPanel.add(serviceCombo, "wrap");
        inputPanel.add(guestLabel);
        inputPanel.add(guestCombo, "wrap");
        inputPanel.add(amountLabel);
        inputPanel.add(amountField, "wrap");

        populateItemCombo(itemCombo, entity, role);
        populateServiceCombo(serviceCombo, entity, role);
        populateGuestCombo(guestCombo, reservation, entity, role);

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton("Salvar");
        saveButton.setFont(new Font("Sans", Font.BOLD, 20));
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                final Expense expense = new Expense();
                expense.setIdExpense(Objects.nonNull(entity) ? entity.getIdExpense() : null);
                expense.setItem(itemRadio.isSelected() && itemCombo.getSelectedIndex() != -1 ? items.get(itemCombo.getSelectedIndex()) : null);
                expense.setService(serviceRadio.isSelected() && serviceCombo.getSelectedIndex() != -1 ? services.get(serviceCombo.getSelectedIndex()) : null);
                expense.setReservation(reservation);
                expense.setGuest(guestCombo.getSelectedIndex() != -1 ? guests.get(guestCombo.getSelectedIndex()) : null);
                if (itemRadio.isSelected()) {
                    if (itemCombo.getSelectedIndex() != -1) {
                        expense.setBranch(items.get(itemCombo.getSelectedIndex()).getBranch());
                    } else {
                        expense.setBranch(null);
                    }
                } else {
                    if (serviceCombo.getSelectedIndex() != -1) {
                        expense.setBranch(services.get(serviceCombo.getSelectedIndex()).getBranch());
                    } else {
                        expense.setBranch(null);
                    }
                }

                try {
                    final int amount = Integer.parseInt(amountField.getText());
                    expense.setAmount(amount);
                } catch (
                        NumberFormatException ex) {
                    expense.setAmount(null);
                }

                final boolean result = Objects.nonNull(entity) ? ExpenseService.update(expense, itemRadio.isSelected() ? 'I' : 'S') : ExpenseService.save(expense, itemRadio.isSelected() ? 'I' : 'S');

                if (result) {
                    JOptionPane.showMessageDialog(this, "Dispesa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    new ExpenseView(role, reservation);
                    dispose();
                }
            } catch (
                    Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar a dispesa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        final JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Sans", Font.BOLD, 20));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new ExpenseView(role, reservation);
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

    private void populateItemCombo(JComboBox<String> itemCombo, Expense entity, Role role) {
        final List<Item> itemList = ItemService.findAll(role);

        if (Objects.nonNull(itemList) && !itemList.isEmpty()) {
            for (Item item : itemList) {
                itemCombo.addItem(item.getName());
                items.add(item);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getItem())) {
            itemCombo.setSelectedItem(entity.getItem().getName());
        }
    }

    private void populateServiceCombo(JComboBox<String> serviceCombo, Expense entity, Role role) {
        final List<Service> serviceList = ServiceService.findAll(role);

        if (Objects.nonNull(serviceList) && !serviceList.isEmpty()) {
            for (Service service : serviceList) {
                serviceCombo.addItem(service.getDescription());
                services.add(service);
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getService())) {
            serviceCombo.setSelectedItem(entity.getService().getDescription());
        }
    }

    private void populateGuestCombo(JComboBox<String> guestCombo, Reservation reservation, Expense entity, Role role) {
        final List<Guest> guestList = GuestService.findAll(role);

        if (Objects.nonNull(guestList) && !guestList.isEmpty()) {
            for (Guest guest : guestList) {
                if (reservation.getGuestList().contains(guest)) {
                    String guestName;

                    if (guest.getIsForeigner()) {
                        guestName = guest.getName() + " - " + guest.getPassportNumber();
                    } else {
                        guestName = guest.getName() + " - " + guest.getCpf();
                    }

                    guestCombo.addItem(guestName);
                    guests.add(guest);
                }
            }
        }

        if (Objects.nonNull(entity) && Objects.nonNull(entity.getGuest())) {
            String guestName;

            if (entity.getGuest().getIsForeigner()) {
                guestName = entity.getGuest().getName() + " - " + entity.getGuest().getPassportNumber();
            } else {
                guestName = entity.getGuest().getName() + " - " + entity.getGuest().getCpf();
            }

            guestCombo.setSelectedItem(guestName);
        }
    }
}
