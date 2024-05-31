package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.BranchService;
import com.felipe.uniroom.services.CorporateService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorporateForm extends JFrame {
    List<User> users = new ArrayList<>();
    public CorporateForm(Role role, Corporate entity) {
        super(Constants.CORPORATE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titlePage = new JLabel(Constants.CORPORATE);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nome da Matriz:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JTextField nameField = new JTextField(20);
        if (Objects.nonNull(entity)) nameField.setText(entity.getName());
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel cnpjLabel = new JLabel("CNPJ:");
        cnpjLabel.setFont(new Font("Sans", Font.BOLD, 20));

        JFormattedTextField cnpjField = new JFormattedTextField(Components.getCnpjFormatter());
        if (Objects.nonNull(entity)) cnpjField.setText(Util.formatCnpj(entity.getCnpj()));
        cnpjField.setPreferredSize(new Dimension(300, 30));
        cnpjField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel userLabel = Components.getLabel(Constants.USER + ":", null, Font.BOLD, null, null);
        final JComboBox<String> userCombo = new JComboBox<>();
        userCombo.setPreferredSize(new Dimension(300, 30));
        userCombo.setFont(Constants.FONT);

        populateUserCombo(userCombo, entity, role);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");
        inputPanel.add(cnpjLabel);
        inputPanel.add(cnpjField, "wrap");
        mainPanel.add(inputPanel, "wrap, grow");
        inputPanel.add(userLabel);
        inputPanel.add(userCombo, "wrap");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final Corporate corporate = new Corporate();
            corporate.setIdCorporate(Objects.nonNull(entity) ? entity.getIdCorporate() : null);
            corporate.setName(nameField.getText());
            corporate.setCnpj(cnpjField.getText());
            corporate.setUser(users.get(userCombo.getSelectedIndex()));
            corporate.setActive(true);

            final Boolean result = Objects.nonNull(entity) ? CorporateService.update(corporate) : CorporateService.save(corporate);

            if (Objects.nonNull(result) && result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new CorporateView(role);
                dispose();
            }
        });

        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new CorporateView(role);
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

    private void populateUserCombo(JComboBox<String> userCombo, Corporate entity, Role role) {
        final List<User> userList = UserRepository.findAll(User.class, role);

        if (Objects.isNull(userList) || userList.isEmpty()) {
            userCombo.addItem("Nenhum usuário cadastrado");
        } else {
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
}
