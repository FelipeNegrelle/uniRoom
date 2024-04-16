package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.BranchService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BranchForm extends JFrame {
    List<Corporate> corporates = new ArrayList<>();
    List<User> users = new ArrayList<>();

    public BranchForm(Role role) {
        super(Constants.BRANCH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.BRANCH);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel("Nome da Filial:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel corporateLabel = Components.getLabel(Constants.CORPORATE, null, Font.BOLD, null, null);
        final JComboBox<String> corporateCombo = new JComboBox<>();
        corporateCombo.setPreferredSize(new Dimension(300, 30));
        corporateCombo.setFont(Constants.FONT);

        populateCorporateCombo(corporateCombo);

        final JLabel userLabel = Components.getLabel(Constants.USER, null, Font.BOLD, null, null);
        final JComboBox<String> userCombo = new JComboBox<>();
        userCombo.setPreferredSize(new Dimension(300, 30));
        userCombo.setFont(Constants.FONT);

        populateUserCombo(userCombo);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");

        inputPanel.add(corporateLabel);
        inputPanel.add(corporateCombo, "wrap");

        inputPanel.add(userLabel);
        inputPanel.add(userCombo, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton(Constants.REGISTER);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final Branch branch = new Branch();
            branch.setName(nameField.getText());
            branch.setCorporate(corporates.get(corporateCombo.getSelectedIndex()));
            branch.setUser(users.get(userCombo.getSelectedIndex()));
            branch.setActive(true);

            final Boolean result = BranchService.save(branch);

            if (result) {
                JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                new BranchView(role);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a filial", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        final JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new BranchView(role);
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

    private void populateCorporateCombo(JComboBox<String> corporateCombo) {
        final List<Corporate> corporateList = CorporateRepository.findAll(Corporate.class);

        if (Objects.isNull(corporateList) || corporateList.isEmpty()) {
            corporateCombo.addItem("Nenhuma matriz cadastrada");
        } else {
            corporateCombo.removeAllItems();

            for (Corporate corporate : corporateList) {
                corporateCombo.addItem(corporate.getName());
                corporates.add(corporate);
            }
        }
    }

    private void populateUserCombo(JComboBox<String> userCombo) {
        final List<User> userList = UserRepository.findAll(User.class);

        if (Objects.isNull(userList) || userList.isEmpty()) {
            userCombo.addItem("Nenhum usuário cadastrado");
        } else {
            userCombo.removeAllItems();

            for (User user : userList) {
                userCombo.addItem(user.getName());
                users.add(user);
            }
        }
    }
}

