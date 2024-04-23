package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CorporateForm extends JFrame {
    public CorporateForm(Role role) {
        super(Constants.CORPORATE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE); // Cor de fundo semelhante ao Constants.BLUE

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
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel cnpjLabel = new JLabel("CNPJ:");
        cnpjLabel.setFont(new Font("Sans", Font.BOLD, 20));

        JFormattedTextField cnpjField = new JFormattedTextField(Components.getCnpjFormatter());
        cnpjField.setPreferredSize(new Dimension(300, 30));
        cnpjField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel usernameLabel = new JLabel("Usuário:");
        usernameLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JTextField usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Sans", Font.PLAIN, 20));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");
        inputPanel.add(cnpjLabel);
        inputPanel.add(cnpjField, "wrap");
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField, "wrap");
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);


        mainPanel.add(inputPanel, "wrap, grow");

        JButton saveButton = new JButton(Constants.REGISTER);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);

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

}
