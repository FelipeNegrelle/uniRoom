package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.services.CorporateService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorporateView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Corporate> searchItens = new ArrayList<>();

    public CorporateView(Role role) {
        super(Constants.CORPORATE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.CORPORATE);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, "align center");

        final JPanel searchPanel = new JPanel(new MigLayout("insets 0", "[right]unrel[grow]unrel[]", "[grow]"));
        searchPanel.setBackground(Constants.BLUE);

        final JButton backButton = new JButton(Constants.BACK);
        backButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
        backButton.setBackground(Constants.WHITE);
        backButton.setIcon(Constants.BACK_ICON);
        backButton.addActionListener(e -> {
            new Home(role);
            dispose();
        });
        searchPanel.add(backButton, "align left ");

        final JButton newCorporate = new JButton(Constants.NEW);
        newCorporate.setBackground(Constants.WHITE);
        newCorporate.setForeground(Constants.BLACK);
        newCorporate.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newCorporate.addActionListener(e -> {
            new CorporateForm(role, null);
            dispose();
        });
        searchPanel.add(newCorporate, "align left");

        final JLabel searchLabel = new JLabel(Constants.SEARCH);
        searchLabel.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setPreferredSize(new Dimension(70, 40));
        searchPanel.add(searchLabel, "align right");

        final JTextField searchField = new JTextField(20);
        searchField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchItens.addAll(CorporateService.search(searchField.getText(), null, role));
                updateCorporateTable(role);
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nome", "CNPJ", "Gerente", "Ativo"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final TableColumn optionsColumn = table.getColumnModel().getColumn(0);
        optionsColumn.setCellRenderer(new Components.OptionsCellRenderer());

        final TableColumn activeColumn = table.getColumnModel().getColumn(5);
        activeColumn.setCellRenderer(new Components.IconCellRenderer());

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Corporate corporate = CorporateRepository.findById(Corporate.class, (int) model.getValueAt(row, 1));

                    new CorporateForm(role, corporate);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Corporate corporate = new Corporate();

                    corporate.setIdCorporate((int) model.getValueAt(row, 1));

                    if (CorporateService.delete(corporate)) {
                        updateCorporateTable(role);
                    } else {
                        Components.showGenericError(this);
                    }
                });

                popupMenu.add(editItem);
                popupMenu.add(deleteItem);

                popupMenu.show(tableEvt, evt.getX(), evt.getY());
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        updateCorporateTable(role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
    }

    private static void updateCorporateTable(Role role) {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Corporate corporate : searchItens) {
                model.addRow(new Object[]{
                        null,
                        corporate.getIdCorporate(),
                        corporate.getName(),
                        Util.formatCnpj(corporate.getCnpj()),
                        corporate.getUser().getName(),
                        corporate.getActive(),
                });
            }
            searchItens.clear();
        } else {
            final List<Corporate> corporateList = CorporateRepository.findAll(Corporate.class, role);
            if (Objects.nonNull(corporateList)) {
                for (Corporate branch : corporateList) {
                    model.addRow(new Object[]{
                            null,
                            branch.getIdCorporate(),
                            branch.getName(),
                            Util.formatCnpj(branch.getCnpj()),
                            branch.getUser().getName(),
                            branch.getActive(),
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }
}
