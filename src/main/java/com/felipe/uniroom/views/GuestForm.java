package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.services.GuestService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuestForm extends JFrame {
    final List<Branch> branches = new ArrayList<>();

    public GuestForm(Role role, Guest entity) {
        super(Constants.GUEST);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.GUEST);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel("Nome do Hóspede:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField nameField = new JTextField(20);
        if (Objects.nonNull(entity)) {
            nameField.setText(entity.getName());
        }
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JCheckBox foreignerCheckBox = new JCheckBox("É estrangeiro?");
        foreignerCheckBox.setFont(new Font("Sans", Font.BOLD, 20));
        foreignerCheckBox.setSelected(Objects.nonNull(entity) && entity.getIsForeigner());

        final JLabel cpfLabel = new JLabel("CPF:");
        cpfLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JFormattedTextField cpfField = new JFormattedTextField(Components.getCpfFormatter());
        if (Objects.nonNull(entity) && !entity.getIsForeigner()) {
            cpfField.setText(Util.formatCpf(entity.getCpf()));
        }
        cpfField.setPreferredSize(new Dimension(300, 30));
        cpfField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel passportLabel = new JLabel("Passaporte:");
        passportLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField passportField = new JTextField(20);
        if (Objects.nonNull(entity) && entity.getIsForeigner()) {
            passportField.setText(entity.getPassportNumber());
        }
        passportField.setPreferredSize(new Dimension(300, 30));
        passportField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel branchLabel = new JLabel("Filial:");
        branchLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(Constants.FONT);

        foreignerCheckBox.addActionListener(e -> {
            boolean isForeigner = foreignerCheckBox.isSelected();
            cpfLabel.setVisible(!isForeigner);
            cpfField.setVisible(!isForeigner);
            passportLabel.setVisible(isForeigner);
            passportField.setVisible(isForeigner);
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");

        inputPanel.add(cpfLabel);
        inputPanel.add(cpfField, "wrap");

        inputPanel.add(passportLabel);
        inputPanel.add(passportField, "wrap");

        inputPanel.add(foreignerCheckBox, "span, wrap");

        inputPanel.add(branchLabel);
        inputPanel.add(branchCombo, "wrap");

        if (Objects.nonNull(entity) && entity.getIsForeigner()) {
            cpfLabel.setVisible(false);
            cpfField.setVisible(false);
        } else {
            passportLabel.setVisible(false);
            passportField.setVisible(false);
        }

        populateBranchCombo(branchCombo, entity, role);

        final JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(new Font("Sans", Font.BOLD, 20));
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final Guest guest = new Guest();
            guest.setIdGuest(Objects.nonNull(entity) ? entity.getIdGuest() : null);
            guest.setName(nameField.getText());
            if (foreignerCheckBox.isSelected()) {
                guest.setIsForeigner(true);
                guest.setPassportNumber(passportField.getText());
                guest.setCpf(null);
            } else {
                guest.setIsForeigner(false);
                guest.setCpf(cpfField.getText());
                guest.setPassportNumber(null);
            }
            guest.setBranch(branches.get(branchCombo.getSelectedIndex()));

            final Boolean result = Objects.nonNull(entity) ? GuestService.update(guest, role) : GuestService.save(guest, role);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new GuestView(role);
                dispose();
            }
        });

        final JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(new Font("Sans", Font.BOLD, 20));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new GuestView(role);
            dispose();
        });

        mainPanel.add(inputPanel, "wrap, grow");
        mainPanel.add(saveButton, "split 2, align center");
        mainPanel.add(cancelButton, "align center, wrap");

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void populateBranchCombo(JComboBox<String> branchCombo, Guest entity, Role role) {
        final List<Branch> branchList = BranchRepository.findAll(Branch.class, role);

        if (Objects.nonNull(branchList) && !branchList.isEmpty()) {
            branchCombo.removeAllItems();
            branches.clear();

            for (Branch branch : branchList) {
                branchCombo.addItem(branch.getName());
                branches.add(branch);
            }
        }

        if (Objects.nonNull(entity)) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }
}