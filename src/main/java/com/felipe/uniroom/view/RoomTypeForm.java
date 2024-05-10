package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.RoomType;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.services.RoomTypeService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomTypeForm extends JFrame {
    List<Branch> branches = new ArrayList<>();

    public RoomTypeForm(Role role, RoomType entity) {
        super(Constants.ROOM_TYPE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 50", "[grow]", "[grow]"));
        getContentPane().setBackground(Constants.BLUE);

        final JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[align center]"));
        mainPanel.setBackground(Color.WHITE);

        final JLabel titlePage = new JLabel(Constants.ROOM_TYPE);
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

        final JLabel capacityLabel = new JLabel("Capacidade:");
        capacityLabel.setFont(new Font("Sans", Font.BOLD, 20));
        final JFormattedTextField capacityField = new JFormattedTextField(Util.getNumberFormatter(0));
        capacityField.setPreferredSize(new Dimension(300, 30));
        capacityField.setFont(new Font("Sans", Font.PLAIN, 20));
        if (Objects.nonNull(entity)) capacityField.setText(String.valueOf(entity.getCapacity()));

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

        inputPanel.add(capacityLabel);
        inputPanel.add(capacityField, "wrap");

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
                RoomType roomType = new RoomType();
                roomType.setIdRoomType(Objects.nonNull(entity) ? entity.getIdRoomType() : null);
                roomType.setName(nameField.getText().trim());

                String priceStr = priceField.getText().trim();
                if (!priceStr.isEmpty()) {
                    NumberFormat format = Util.getNumberFormatter(2);
                    Number number = format.parse(priceStr);
                    roomType.setPrice(number.floatValue());
                } else {
                    roomType.setPrice(null);
                }

                String capacityStr = capacityField.getText().trim();
                if (!capacityStr.isEmpty()) {
                    roomType.setCapacity(Byte.parseByte(capacityStr));
                } else {
                    roomType.setCapacity(null);
                }

                roomType.setBranch(branches.get(branchCombo.getSelectedIndex()));
                roomType.setActive(true);

                boolean result = Objects.nonNull(entity) ? RoomTypeService.update(roomType) : RoomTypeService.save(roomType);

                if (result) {
                    JOptionPane.showMessageDialog(this, Constants.SUCCESSFUL_REGISTER, "Sucesso", JOptionPane.PLAIN_MESSAGE);
                    new RoomTypeView(role);
                    dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos para preço e capacidade.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                System.out.println(ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o tipo de quarto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });


        final JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Sans", Font.BOLD, 20));
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            new RoomTypeView(role);
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

    private void populateBranchCombo(JComboBox<String> branchCombo, RoomType entity) {
        List<Branch> branchList = BranchRepository.findAll(Branch.class);
        for (Branch branch : branchList) {
            branchCombo.addItem(branch.getName());
            branches.add(branch);
        }

        if (Objects.nonNull(entity)) {
            branchCombo.setSelectedItem(entity.getBranch().getName());
        }
    }
}

