package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.services.ItemService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemForm extends JFrame {
    List<Branch> branches = new ArrayList<>();

    public ItemForm(Role role, Item entity) {
        super(Constants.ITEM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.ITEM);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Constants.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity))
            nameField.setText(entity.getName());

        final JLabel priceLabel = new JLabel("Preço:");
        priceLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JFormattedTextField priceField = new JFormattedTextField(Util.getNumberFormatter(2));
        priceField.setPreferredSize(new Dimension(300, 30));
        priceField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity))
            priceField.setValue(entity.getPrice());

        final JLabel branchLabel = new JLabel("Filial:");
        branchLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        populateBranchCombo(branchCombo, entity);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField, "wrap");

        inputPanel.add(priceLabel);
        inputPanel.add(priceField, "wrap");

        inputPanel.add(branchLabel);
        inputPanel.add(branchCombo, "wrap");

        mainPanel.add(inputPanel, "wrap, grow");

        final JButton saveButton = new JButton("Salvar");
        saveButton.setFont(new Font("Sans", Font.BOLD, 20));
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setBackground(Constants.BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                Item item = new Item();
                item.setIdItem(Objects.nonNull(entity) ? entity.getIdItem() : null);
                item.setName(nameField.getText().trim());
                item.setActive(true);
                String priceStr = priceField.getText().trim();
                if (!priceStr.isEmpty()) {
                    NumberFormat format = Util.getNumberFormatter(2);
                    Number number = format.parse(priceStr);
                    item.setPrice(number.floatValue());
                } else {
                    item.setPrice(null);
                }

                item.setBranch(branches.get(branchCombo.getSelectedIndex()));

                boolean result = Objects.nonNull(entity) ? ItemService.update(item) : ItemService.save(item);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Item salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    new ItemView(role);
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        final JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Sans", Font.BOLD, 20));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
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

    private void populateBranchCombo(JComboBox<String> branchCombo, Item entity) {
        List<Branch> branchList = BranchRepository.findAll(Branch.class);
        for (Branch branch : branchList) {
            branchCombo.addItem(branch.getName());
            branches.add(branch);
        }

        if (Objects.nonNull(entity) && entity.getBranch() != null) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }
}
