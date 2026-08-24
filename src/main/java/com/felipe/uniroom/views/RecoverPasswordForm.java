package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.services.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

public class RecoverPasswordForm extends JFrame {
    User foundUser = null;

    public RecoverPasswordForm() {
        super(Constants.RECOVER_PASSWORD);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        JLabel titlePage = new JLabel(Constants.RECOVER_PASSWORD);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        JLabel secretPhraseLabel = Components.getLabel(Constants.SECRET_PHRASE + ": ", null, Font.BOLD, null, null);
        JTextField secretPhraseField = new JTextField(20);
        secretPhraseField.setEditable(false);
        secretPhraseField.setPreferredSize(new Dimension(300, 30));
        secretPhraseField.setFont(new Font("Sans", Font.PLAIN, 20));

        JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Nome da Usuário:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));
        JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                final User user = UserService.findByUsername(nameField.getText());
                if (user != null) {
                    foundUser = user;
                    nameField.setText(user.getUsername());
                    secretPhraseField.setText(user.getSecretPhrase());
                }
            }
        });

        JLabel secretAnswerLabel = Components.getLabel(Constants.SECRET_ANSWER + ": ", null, Font.BOLD, null, null);
        JTextField secretAnswerField = new JTextField(20);
        secretAnswerField.setPreferredSize(new Dimension(300, 30));
        secretAnswerField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel newPasswordLabel = Components.getLabel("Nova senha: ", null, Font.BOLD, null, null);
        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setPreferredSize(new Dimension(300, 30));
        newPasswordField.setFont(new Font("Sans", Font.PLAIN, 20));

        JLabel confirmNewPasswordLabel = Components.getLabel(Constants.CONFIRM_PASSWORD + ": ", null, Font.BOLD, null, null);
        JPasswordField confirmNewPasswordField = new JPasswordField(20);
        confirmNewPasswordField.setPreferredSize(new Dimension(300, 30));
        confirmNewPasswordField.setFont(new Font("Sans", Font.PLAIN, 20));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");
        inputPanel.add(secretPhraseLabel);
        inputPanel.add(secretPhraseField, "wrap");
        inputPanel.add(secretAnswerLabel);
        inputPanel.add(secretAnswerField, "wrap");
        inputPanel.add(newPasswordLabel);
        inputPanel.add(newPasswordField, "wrap");
        inputPanel.add(confirmNewPasswordLabel);
        inputPanel.add(confirmNewPasswordField, "wrap");
        mainPanel.add(inputPanel, "wrap, grow");

        JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            if (foundUser == null) {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!");
            } else if (!new String(newPasswordField.getPassword()).equals(new String(confirmNewPasswordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!");
            } else {
                final User result = UserService.forgotPassword(foundUser, secretAnswerField.getText(), newPasswordField.getText(), confirmNewPasswordField.getText());

                if (Objects.nonNull(result)) {
                    JOptionPane.showMessageDialog(this, "Senha atualizada com sucesso!");

                    final Role role = UserService.getUserRole(result);

                    if (Objects.nonNull(role)) {
                        new Home(role);
                    } else {
                        new Login();
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao trocar a senha!");
                }
            }
        });

        JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new Login();
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
