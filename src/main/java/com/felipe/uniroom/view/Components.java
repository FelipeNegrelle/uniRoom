package com.felipe.uniroom.view;

import java.util.Arrays;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.services.UserService;

import java.awt.Font;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;

public class Components {
    // public static class RegistrationDialog extends JDialog {
    // public RegistrationDialog(JFrame parent) {
    // super(parent, Constants.REGISTER, true);
    // setSize(800, 600);
    // setLocationRelativeTo(parent);

    // final JPanel panel = new JPanel(
    // new MigLayout("filly", "[align right][grow]", "[]10[]10[]10[]10[]10[]10[]"));

    // final JLabel nameLabel = getLabel(Constants.NAME, null, Font.BOLD, null,
    // null);

    // final JTextField nameField = new JTextField(30);
    // nameField.setFont(Constants.FONT);
    // nameField.setPreferredSize(Constants.TEXT_FIELD_SIZE);
    // panel.add(nameLabel, "alignx left");
    // panel.add(nameField, "growx, wrap");

    // final JLabel numberLabel = getLabel(Constants.NUMBER, null, Font.BOLD, null,
    // null);

    // final JTextField numberField = new JTextField(30);
    // numberField.setFont(new Font("Sans", Font.PLAIN, 20));
    // numberField.setPreferredSize(Constants.TEXT_FIELD_SIZE);
    // panel.add(numberLabel, "alignx left");
    // panel.add(numberField, "growx, wrap");

    // final JLabel positionLabel = new GenericLabel(POSITION, Font.BOLD)
    // final JComboBox<String> positionField = new JComboBox<>();
    // positionField.addItem("Atacante");
    // positionField.addItem("Meio-campo");
    // positionField.addItem("Lateral");
    // positionField.addItem("Zagueiro");
    // positionField.addItem("Goleiro");
    // positionField.setPreferredSize(new Dimension(300, 10));
    // positionField.setFont(new Font("Sans", Font.PLAIN, 20));
    // panel.add(positionLabel, "alignx left");
    // panel.add(positionField, "growx, wrap");

    // final GenericLabel usernameLabel = new GenericLabel(USER, Font.BOLD);
    // JTextField usernameField = new JTextField(30);
    // usernameField.setFont(new Font("Sans", Font.PLAIN, 20));
    // usernameField.setPreferredSize(new Dimension(300, 10));
    // panel.add(usernameLabel, "alignx left");
    // panel.add(usernameField, "growx, wrap");

    // final GenericLabel passwordLabel = new GenericLabel(PASSWORD, Font.BOLD);
    // JPasswordField passwordField = new JPasswordField(30);
    // passwordField.setFont(new Font("Sans", Font.PLAIN, 20));
    // passwordField.setPreferredSize(new Dimension(300, 10));
    // panel.add(passwordLabel, "alignx left");
    // panel.add(passwordField, "growx, wrap");

    // final GenericLabel questionLabel = new GenericLabel(SECRET_PHRASE,
    // Font.BOLD);
    // final JComboBox<String> questionField = new JComboBox<>();
    // questionField.addItem("Qual seu jogador de futebol favorito?");
    // questionField.addItem("Qual seu time de futebol favorito?");
    // questionField.addItem("Qual posição você joga?");
    // questionField.setPreferredSize(new Dimension(300, 10));
    // questionField.setFont(new Font("Sans", Font.PLAIN, 20));
    // panel.add(questionLabel, "alignx left");
    // panel.add(questionField, "growx, wrap");

    // final GenericLabel answerLabel = new GenericLabel(SECRET_ANSWER, Font.BOLD);
    // JTextField answerField = new JTextField(30);
    // answerField.setFont(new Font("Sans", Font.PLAIN, 20));
    // answerField.setPreferredSize(new Dimension(300, 10));
    // panel.add(answerLabel, "alignx left");
    // panel.add(answerField, "growx, wrap");

    // JButton registerButton = new JButton(REGISTER);
    // registerButton.setFont(new Font("Sans", Font.BOLD, 20));
    // registerButton.setPreferredSize(new Dimension(300, 40));
    // registerButton.setBackground(GREEN);
    // registerButton.setForeground(Color.white);
    // panel.add(registerButton, "span 2, align center");

    // registerButton.addActionListener(e -> {
    // if (Util.isNumber(numberField.getText())
    // && Objects.nonNull(positionField.getSelectedItem())
    // && Objects.nonNull(usernameField.getText())
    // && Objects.nonNull(passwordField.getPassword())
    // && Objects.nonNull(questionField.getSelectedItem())
    // && Objects.nonNull(answerField.getText())) {
    // boolean registrationSuccess = Auth.register(
    // nameField.getText(),
    // numberField.getText(),
    // positionField.getSelectedItem().toString(),
    // usernameField.getText(),
    // Arrays.toString(passwordField.getPassword()),
    // questionField.getSelectedItem().toString(),
    // answerField.getText());

    // if (registrationSuccess) {
    // JOptionPane.showMessageDialog(RegistrationDialog.this, SUCCESSFUL_REGISTER);
    // } else {
    // JOptionPane.showMessageDialog(RegistrationDialog.this, FAILED_REGISTER,
    // Constants.ERROR,
    // JOptionPane.ERROR_MESSAGE);
    // }
    // } else {
    // System.out.println(numberField.getText());
    // JOptionPane.showMessageDialog(RegistrationDialog.this, FAILED_REGISTER,
    // Constants.ERROR,
    // JOptionPane.ERROR_MESSAGE);
    // }
    // });

    // add(panel);
    // }
    // }

    public static JLabel getLabel(String text, String fontFamily, Integer fontStyle, Integer fontSize, Color color) {
        final JLabel label = new JLabel(text);

        final Font font = new Font(
                !Objects.isNull(fontFamily) && !fontFamily.isEmpty() ? fontFamily : "Sans",
                !Objects.isNull(fontStyle) && fontStyle >= 0 ? fontStyle : Font.PLAIN,
                !Objects.isNull(fontSize) && fontSize > 0 ? fontSize : 20);
        label.setFont(font);

        if (Objects.nonNull(color)) {
            label.setForeground(color);
        } else {
            label.setForeground(Constants.BLACK);
        }

        return label;
    }
}
