package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Menu;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    public Home(Role role) {
        super(Constants.UNIROOM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]50[grow]", "[]"));
        mainPanel.setBackground(Constants.WHITE);

        final JLabel titlePage = Components.getLabel(Constants.UNIROOM, null, Font.BOLD, 60, Constants.WHITE);
        add(titlePage, "align center, wrap, span");

        final JLabel userLabel = Components.getLabel("Bem-vindo, " + role.getUser().getName(), null, Font.BOLD, 20, Constants.BLACK);
        mainPanel.add(userLabel, "split, span, align center");

        final JButton exit = new JButton("", Constants.EXIT_ICON);
        exit.setFont(Constants.FONT.deriveFont(Font.BOLD));
        exit.setPreferredSize(new Dimension(10, 10));
        exit.setBackground(Constants.RED);
        exit.setForeground(Constants.WHITE);
        exit.addActionListener(e -> {
            new Login();

            dispose();
        });
        mainPanel.add(exit, "align center, wrap");

        final JPanel gridPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[align top]"));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        final JPanel buttonPanel = new JPanel(new MigLayout("wrap 3", "[grow][grow][grow]", "[]20[]"));
        buttonPanel.setBackground(Constants.WHITE);

        for (Menu menu : role.getMenus()) {
            final JButton button = new JButton(menu.getName(), menu.getIcon());
            button.setFont(Constants.FONT.deriveFont(Font.BOLD));
            button.setPreferredSize(Constants.BUTTON_SIZE);
            button.setBackground(Constants.YELLOW);
            button.setForeground(Constants.BLACK);
            button.addActionListener(e -> {
                try {
                    menu.getDestination().getDeclaredConstructor(Role.class).newInstance(role);

                    dispose();
                } catch (
                        Exception ex) {
                    ex.printStackTrace();
                }
            });

            buttonPanel.add(button, "grow");
        }

        gridPanel.add(buttonPanel, "grow");
        mainPanel.add(scrollPane, "grow, push, wrap");

        add(mainPanel, "align center");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
