package com.felipe.uniroom.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.felipe.uniroom.config.Constants;
import net.miginfocom.swing.MigLayout;

public class Home extends JFrame {
    public Home() {
        super(Constants.UNIROOM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Constants.WHITE);

        final JLabel titlePage = Components.getLabel(Constants.UNIROOM, null, Font.BOLD, 60, Constants.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel gridPanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[align top]"));
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        final JPanel buttonPanel = new JPanel(new MigLayout("wrap 3", "[grow][grow][grow]", "[]20[]"));
        buttonPanel.setBackground(Constants.WHITE);

        for (int i = 1; i <= 6; i++) {
            JButton button = new JButton("button  " + i);
            button.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));
            button.setPreferredSize(new Dimension(200, 50));
            button.setBackground(Constants.BLUE);
            button.setForeground(Constants.WHITE);
            buttonPanel.add(button, "grow");
        }

        gridPanel.add(buttonPanel, "grow");
        mainPanel.add(scrollPane, "grow, push, wrap");

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
