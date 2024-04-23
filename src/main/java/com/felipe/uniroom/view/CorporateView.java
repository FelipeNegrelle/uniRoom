package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.repositories.CorporateRepository;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class CorporateView extends JFrame {
    private static DefaultTableModel model;

    public CorporateView(Role role) {
        super(Constants.CORPORATE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));

        final JPanel panel = new JPanel(new MigLayout("align center, wrap 4", "[grow]", "[][grow][]"));
        panel.setBackground(Constants.BLUE);

        final JLabel titleLabel = new JLabel(Constants.CORPORATE);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, "span, center, gapbottom 15");

        model = new DefaultTableModel(new Object[]{"Código", "Nome", "CNPJ", "Gerente", "Ativo"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionBackground(Constants.BLUE);
        table.setSelectionForeground(Color.WHITE);

        final Components.IconCellRenderer iconCellRenderer = new Components.IconCellRenderer();

        final TableColumn activeColumn = table.getColumnModel().getColumn(4);
        activeColumn.setCellRenderer(iconCellRenderer);

        final JButton returnButton = new JButton(Constants.BACK);
        returnButton.addActionListener(e -> {
            new Home(role);
            dispose();
        });
        returnButton.setBackground(Constants.RED);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));

        final JButton newCorporate = new JButton(Constants.NEW);
        newCorporate.setBackground(Constants.WHITE);
        newCorporate.setForeground(Constants.BLACK);
        newCorporate.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        newCorporate.addActionListener(e -> {
            new CorporateForm(role);
            dispose();
            updateCorporateTable();
        });

        final JButton editCorporate = new JButton(Constants.EDIT);
        editCorporate.setBackground(Constants.WHITE);
        editCorporate.setForeground(Constants.BLACK);
        editCorporate.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        editCorporate.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
//                    Corporate Corporate = new Corporate();

//                    Corporate.setId((int) model.getValueAt(selectedRow, 0));
//                    Corporate.setName((String) model.getValueAt(selectedRow, 1));
//                    Corporate.setNumber((int) model.getValueAt(selectedRow, 2));
//                    Corporate.setPosition((String) model.getValueAt(selectedRow, 3));

//                    new Components.EditCorporateDialog(this, Corporate).setVisible(true);

                updateCorporateTable();
            } else {
                JOptionPane.showMessageDialog(this, Constants.EDIT_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        final JButton deleteCorporate = new JButton(Constants.DELETE);
        deleteCorporate.setBackground(Constants.RED);
        deleteCorporate.setForeground(Color.WHITE);
        deleteCorporate.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        deleteCorporate.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
//                    Corporate Corporate = new Corporate();
//                    Corporate.setId((int) model.getValueAt(selectedRow, 0));

//                    new Components.DeleteDialog(this, Corporate).setVisible(true);

                updateCorporateTable();
            } else {
                JOptionPane.showMessageDialog(this, Constants.DELETE_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        updateCorporateTable();

        table.getSelectionModel().addListSelectionListener(e -> table.repaint());
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "wrap, align center, grow");
        panel.add(returnButton, "split 4, align left, grow");
        panel.add(newCorporate, "align right, grow");
        panel.add(editCorporate, "align center, grow");
        panel.add(deleteCorporate, "align right, grow");

        add(panel, "align center, grow");
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void updateCorporateTable() {
        model.setRowCount(0);

        final List<Corporate> corporateList = CorporateRepository.findAll(Corporate.class);
        if (Objects.nonNull(corporateList)) {
            for (Corporate corporate : corporateList) {
                model.addRow(new Object[]{
                        corporate.getIdCorporate(),
                        corporate.getName(),
                        corporate.getCnpj(),
                        corporate.getUser().getName(),
                        corporate.isActive()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
        }
    }
}
