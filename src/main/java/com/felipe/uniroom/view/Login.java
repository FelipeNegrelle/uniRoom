package com.felipe.uniroom.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.felipe.uniroom.Constants;
import com.felipe.uniroom.controller.Auth;

import net.miginfocom.swing.MigLayout;

public class Login extends JFrame {
    public Login() {
        super(Constants.LOGIN);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Constants.WHITE);

        final JLabel titlePage = new JLabel(Constants.UNIROOM);
        titlePage.setFont(Constants.FONT.deriveFont(Font.BOLD, 60));
        titlePage.setForeground(Constants.WHITE);
        add(titlePage, "align center");

        final JLabel titleLabel = new JLabel(Constants.LOGIN);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Constants.BLACK);
        mainPanel.add(titleLabel, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        final JLabel userLabel = new JLabel(Constants.USER);
        userLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final JLabel passwordLabel = new JLabel(Constants.PASSWORD);
        passwordLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final JTextField userField = new JTextField(20);
        userField.setPreferredSize(new Dimension(300, 30));
        userField.setFont(Constants.FONT.deriveFont(Font.PLAIN, 20));

        final JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(Constants.FONT.deriveFont(Font.PLAIN, 20));

        inputPanel.add(userLabel);
        inputPanel.add(userField, "wrap");
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton loginButton = new JButton(Constants.LOGIN);
        loginButton.setFont(new Font("Sans", Font.BOLD, 20));
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setBackground(Constants.BLUE);
        loginButton.setForeground(Constants.GRAY);
        loginButton.addActionListener(e -> {
            try {
                // if (!Auth.login()) {
                // System.out.println(userField.getText() +
                // Arrays.toString(passwordField.getPassword()));
                // JOptionPane.showMessageDialog(this, Constants.FAILED_LOGIN);
                // return;
                // }
                // new Menu();
                // Thread.sleep(50);
                // dispose();

                System.out.println("Login button clicked");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        final JButton registerButton = new JButton(Constants.REGISTER);
        registerButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setBackground(Constants.BLUE);
        registerButton.setForeground(Constants.GRAY);
        registerButton.addActionListener(e -> {
            try {
                // new Components.RegistrationDialog(this).setVisible(true);
                System.out.println("Register button clicked");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        final JButton forgetPasswordButton = new JButton(Constants.FORGOT);
        forgetPasswordButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        forgetPasswordButton.setPreferredSize(new Dimension(300, 40));
        forgetPasswordButton.setBackground(Constants.BLUE);
        forgetPasswordButton.setForeground(Constants.GRAY);
        forgetPasswordButton.addActionListener(e -> {
            try {
                // new Components.GetUserForgetPasswordDialog(this).setVisible(true);
                System.out.println("Forget password button clicked");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        mainPanel.add(registerButton, "align center, split 2");
        mainPanel.add(loginButton, "align center, wrap");
        mainPanel.add(forgetPasswordButton, "grow");
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}