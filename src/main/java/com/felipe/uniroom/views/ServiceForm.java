package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Service;
import com.felipe.uniroom.services.BranchService;
import com.felipe.uniroom.services.ServiceService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceForm extends JFrame {
    List<Branch> branches = new ArrayList<>();

    public ServiceForm(Role role, Service entity) {
        super(Constants.SERVICE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.SERVICE);
        titlePage.setFont(new Font("Sans", Font.BOLD, 60));
        titlePage.setForeground(Constants.WHITE);
        add(titlePage, "align center, wrap");

        final JPanel inputPanel = new JPanel(new MigLayout("fillx, insets 20", "[grow]", "[]10[]"));
        inputPanel.setBackground(Color.WHITE);

        final JLabel nameLabel = new JLabel("Descrição:");
        nameLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 30));
        nameField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity))
            nameField.setText(entity.getDescription());

        final JLabel priceLabel = new JLabel("Preço:");
        priceLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JFormattedTextField priceField = new JFormattedTextField(Util.getNumberFormatter(2));
        priceField.setPreferredSize(new Dimension(300, 30));
        priceField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity))
            priceField.setValue(entity.getPrice());

        final JLabel branchLabel = new JLabel(Constants.BRANCH + ": ");
        branchLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JComboBox<String> branchCombo = new JComboBox<>();
        branchCombo.setPreferredSize(new Dimension(300, 30));
        branchCombo.setFont(new Font("Sans", Font.PLAIN, 20));
        populateBranchCombo(branchCombo, entity, role);

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
                final Service service = new Service();
                service.setIdService(Objects.nonNull(entity) ? entity.getIdService() : null);
                service.setDescription(nameField.getText().trim());
                final String priceStr = priceField.getText().trim();
                if (!priceStr.isEmpty()) {
                    final NumberFormat format = Util.getNumberFormatter(2);
                    final Number number = format.parse(priceStr);
                    service.setPrice(number.floatValue());
                } else {
                    service.setPrice(null);
                }

                service.setBranch(branches.get(branchCombo.getSelectedIndex()));

                boolean result = Objects.nonNull(entity) ? ServiceService.update(service) : ServiceService.save(service);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Serviço salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    new ServiceView(role);
                    dispose();
                }
            } catch (
                    Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o serviço: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        final JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Sans", Font.BOLD, 20));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new ServiceView(role);
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

    private void populateBranchCombo(JComboBox<String> branchCombo, Service entity, Role role) {
        List<Branch> branchList = BranchService.findAll(role);

        if (Objects.nonNull(branchList) && !branchList.isEmpty()) {
            for (Branch branch : branchList) {
                branchCombo.addItem(branch.getName());
                branches.add(branch);
            }
        }

        if (Objects.nonNull(entity) && entity.getBranch() != null) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }
}
