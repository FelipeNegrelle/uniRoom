package com.felipe.uniroom.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.UserService;

import net.miginfocom.swing.MigLayout;

public class Login extends JFrame {
    public Login() {
        super(Constants.LOGIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Constants.WHITE);

        final JLabel titlePage = Components.getLabel(Constants.UNIROOM, null, Font.BOLD, 60, Constants.WHITE);
        add(titlePage, "align center");

        final JLabel titleLabel = Components.getLabel(Constants.LOGIN, null, Font.BOLD, 40, null);
        mainPanel.add(titleLabel, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        final JLabel userLabel = Components.getLabel(Constants.USER, null, Font.BOLD, null, null);
        userLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final JLabel passwordLabel = Components.getLabel(Constants.PASSWORD, null, Font.BOLD, null, null);

        final JTextField userField = new JTextField(20);
        userField.setPreferredSize(Constants.TEXT_FIELD_SIZE);
        userField.setFont(Constants.FONT);

        final JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(Constants.TEXT_FIELD_SIZE);
        passwordField.setFont(Constants.FONT);

        inputPanel.add(userLabel);
        inputPanel.add(userField, "wrap");
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton loginButton = new JButton(Constants.LOGIN);
        loginButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        loginButton.setPreferredSize(Constants.BUTTON_SIZE);
        loginButton.setBackground(Constants.BLUE);
        loginButton.setForeground(Constants.WHITE);
        loginButton.addActionListener(e -> {
            try {
                User user = new User();
                user.setUsername(userField.getText());
                user.setPassword(passwordField.getText());

                if (!UserService.login(user)) {
                    System.out.println(user.getUsername());
                    System.out.println(user.getPassword());

                    JOptionPane.showMessageDialog(this, Constants.FAILED_LOGIN);
                } else {
                    user = UserRepository.findByUsername(user.getUsername());

                    JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_LOGIN);

                    Thread.sleep(50);

                    dispose();

                    if (user != null) {
                        new Home(Util.getRoleUser(user));
                    } else {
                        JOptionPane.showMessageDialog(this, Constants.GENERIC_ERROR);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        final JButton forgetPasswordButton = new JButton(Constants.FORGOT);
        forgetPasswordButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        forgetPasswordButton.setPreferredSize(new Dimension(300, 40));
        forgetPasswordButton.setBackground(Constants.BLUE);
        forgetPasswordButton.setForeground(Constants.WHITE);
        forgetPasswordButton.addActionListener(e -> {
            try {
                new Components.ForgetPasswordDialog(this, userField.getText()).setVisible(true);
                System.out.println("Forget password button clicked");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        mainPanel.add(loginButton, "align center, split 2");
        mainPanel.add(forgetPasswordButton, "align center, wrap");
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}