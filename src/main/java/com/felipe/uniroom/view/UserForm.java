package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.services.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UserForm extends JFrame {
    public UserForm(Role role, User entity) {
        super(Constants.USER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.USER);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Color.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel(Constants.NAME+ ":");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField nameField = new JTextField(20);
        if (Objects.nonNull(entity)) nameField.setText(entity.getName());
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel usernameLabel = new JLabel(Constants.USER + ":");
        usernameLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField usernameField = new JTextField(20);
        if (Objects.nonNull(entity)) usernameField.setText(entity.getUsername());
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel passwordLabel = new JLabel(Constants.PASSWORD+ ":");
        passwordLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Sans", Font.PLAIN, 20));

        final JLabel roleLabel = new JLabel("Cargo:");
        roleLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JComboBox<Character> roleCombo = new JComboBox<>(new Character[]{'A', 'M', 'E'});
        roleCombo.setPreferredSize(new Dimension(300, 30));
        roleCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity)) roleCombo.setSelectedItem(entity.getRole());

        final JLabel secretQuestionLabel = new JLabel(Constants.SECRET_PHRASE + ":");
        secretQuestionLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JComboBox<String> secretQuestionCombo = new JComboBox<>(new String[]{
                "Qual é o nome do seu animal de estimação?",
                "Qual é o nome da sua cidade natal?",
                "Qual é o nome do seu melhor amigo de infância?",
                "Qual é o nome do seu primeiro professor?",
                "Qual é o nome do seu filme favorito?"
        });
        secretQuestionCombo.setPreferredSize(new Dimension(500, 30));
        secretQuestionCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity)) {
            String currentSecretQuestion = entity.getSecretPhrase();
            secretQuestionCombo.setSelectedItem(currentSecretQuestion);
        }

        final JLabel secretAnswerLabel = new JLabel(Constants.SECRET_ANSWER + ":");
        secretAnswerLabel.setFont(new Font("Sans", Font.BOLD, 20));

        final JTextField secretAnswerField = new JTextField(20);
        secretAnswerField.setPreferredSize(new Dimension(300, 30));
        secretAnswerField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity)) secretAnswerField.setText(entity.getSecretAnswer());

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField, "wrap");

        if (entity == null) {
            inputPanel.add(passwordLabel);
            inputPanel.add(passwordField, "wrap");
        }

        inputPanel.add(roleLabel);
        inputPanel.add(roleCombo, "wrap");

        inputPanel.add(secretQuestionLabel);
        inputPanel.add(secretQuestionCombo, "wrap");

        inputPanel.add(secretAnswerLabel);
        inputPanel.add(secretAnswerField, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton(Constants.SAVE);
        saveButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        saveButton.setPreferredSize(Constants.BUTTON_SIZE);
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            final User user = new User();
            user.setIdUser(Objects.nonNull(entity) ? entity.getIdUser() : null);
            user.setName(nameField.getText());
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setRole((Character) roleCombo.getSelectedItem());
            user.setSecretPhrase((String) secretQuestionCombo.getSelectedItem());
            user.setSecretAnswer(secretAnswerField.getText());
            user.setActive(true);

            try {
                final Boolean result = Objects.nonNull(entity) ? UserService.update(user) : UserService.register(user);
                if (result != null && result) {
                    JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, Constants.SUCCESS, JOptionPane.PLAIN_MESSAGE);
                    new UserView(role);
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), Constants.WARN, JOptionPane.PLAIN_MESSAGE);
                ex.printStackTrace();
            }
        });

        final JButton cancelButton = new JButton(Constants.BACK);
        cancelButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
        cancelButton.setPreferredSize(Constants.BUTTON_SIZE);
        cancelButton.setBackground(Constants.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new UserView(role);
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
