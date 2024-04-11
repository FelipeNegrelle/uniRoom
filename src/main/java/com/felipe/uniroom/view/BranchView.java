package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.repositories.CorporateRepository;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class BranchView extends JFrame {
    private static DefaultTableModel model;
    public BranchView(Role role) {
        super(Constants.BRANCH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));

        final JPanel panel = new JPanel(new MigLayout("align center, wrap 4", "[grow]", "[][grow][]"));
        panel.setBackground(Constants.BLUE);

        final JLabel titleLabel = new JLabel(Constants.BRANCH);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, "span, center, gapbottom 15");

        model = new DefaultTableModel(new Object[]{"Código", "Nome", "Matriz", "Gerente", "Ativo"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionBackground(Constants.BLUE);
        table.setSelectionForeground(Color.WHITE);

        final JButton returnButton = new JButton(Constants.BACK);
        returnButton.addActionListener(e -> {
            new Home(role);
            dispose();
        });
        returnButton.setBackground(Constants.RED);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));

        final JButton newBranch = new JButton(Constants.NEW);
        newBranch.setBackground(Constants.WHITE);
        newBranch.setForeground(Constants.BLACK);
        newBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        newBranch.addActionListener(e -> {
            new BranchCreationForm();
            dispose();
            updateBranchTable();
        });

        final JButton editBranch = new JButton(Constants.EDIT);
        editBranch.setBackground(Constants.WHITE);
        editBranch.setForeground(Constants.BLACK);
        editBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        editBranch.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
//                    Branch Branch = new Branch();

//                    Branch.setId((int) model.getValueAt(selectedRow, 0));
//                    Branch.setName((String) model.getValueAt(selectedRow, 1));
//                    Branch.setNumber((int) model.getValueAt(selectedRow, 2));
//                    Branch.setPosition((String) model.getValueAt(selectedRow, 3));

//                    new Components.EditBranchDialog(this, Branch).setVisible(true);

                updateBranchTable();
            } else {
                JOptionPane.showMessageDialog(this, Constants.EDIT_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        final JButton deleteBranch = new JButton(Constants.DELETE);
        deleteBranch.setBackground(Constants.RED);
        deleteBranch.setForeground(Color.WHITE);
        deleteBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        deleteBranch.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
//                    Branch Branch = new Branch();
//                    Branch.setId((int) model.getValueAt(selectedRow, 0));

//                    new Components.DeleteDialog(this, Branch).setVisible(true);

                updateBranchTable();
            } else {
                JOptionPane.showMessageDialog(this, Constants.DELETE_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        updateBranchTable();

        table.getSelectionModel().addListSelectionListener(e -> table.repaint());
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "wrap, align center, grow");
        panel.add(returnButton, "split 4, align left, grow");
        panel.add(newBranch, "align right, grow");
        panel.add(editBranch, "align center, grow");
        panel.add(deleteBranch, "align right, grow");

        add(panel, "align center, grow");
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void updateBranchTable() {
        model.setRowCount(0);

        final List<Branch> branchList = CorporateRepository.findAll(Branch.class);
        if (Objects.nonNull(branchList)) {
            for (Branch branch : branchList) {
                model.addRow(new Object[]{
                        branch.getIdBranch(),
                        branch.getName(),
                        branch.getCorporate().getName(),
                        branch.getUser().getName(),
                        branch.isActive()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
        }
    }
}
