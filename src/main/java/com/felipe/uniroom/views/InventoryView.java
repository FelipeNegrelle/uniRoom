package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.services.InventoryService;
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

public class InventoryView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Inventory> searchItems = new ArrayList<>();

    public InventoryView(Role role) {
        super(Constants.INVENTORY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.INVENTORY);
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
        searchPanel.add(backButton, "align left");

        final JButton newInventory = new JButton(Constants.NEW);
        newInventory.setBackground(Constants.WHITE);
        newInventory.setForeground(Constants.BLACK);
        newInventory.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newInventory.addActionListener(e -> {
            new InventoryForm(role, null);
            dispose();
        });
        searchPanel.add(newInventory, "align left");

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
                searchItems.clear();
                searchItems.addAll(InventoryService.search(searchField.getText(), null, role));
                updateInventoryTable(role);
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", Constants.ROOM, "Filial","Descrição", "Ativo"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final Components.IconCellRenderer iconCellRenderer = new Components.IconCellRenderer();
        final TableColumn activeColumn = table.getColumnModel().getColumn(5);
        activeColumn.setCellRenderer(iconCellRenderer);

        final Components.OptionsCellRenderer optionsCellRenderer = new Components.OptionsCellRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(optionsCellRenderer);

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final JMenuItem itemsItem = new JMenuItem(Constants.ITEMS);
                itemsItem.setIcon(Constants.ITEM_ICON);
                itemsItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                itemsItem.addActionListener(e -> {
                    final Inventory inventory = InventoryService.findById((Integer) model.getValueAt(row, 1));
                    new InventoryItemView(role, inventory);
                    dispose();
                });

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Inventory inventory = InventoryService.findById((Integer) model.getValueAt(row, 1));
                    new InventoryForm(role, inventory);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Inventory inventory = new Inventory();
                    inventory.setIdInventory((Integer) model.getValueAt(row, 1));
                    if (InventoryService.delete(inventory)) {
                        updateInventoryTable(role);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar item", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                popupMenu.add(itemsItem);
                popupMenu.add(editItem);
                popupMenu.add(deleteItem);

                popupMenu.show(tableEvt, evt.getX(), evt.getY());
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        updateInventoryTable(role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateInventoryTable(Role role) {
        model.setRowCount(0);

        if (!searchItems.isEmpty()) {
            for (Inventory inventory : searchItems) {
                model.addRow(new Object[]{
                        null,
                        inventory.getIdInventory(),
                        inventory.getRoom().getRoomNumber(),
                        inventory.getBranch().getName(),
                        inventory.getDescription(),
                        inventory.getActive()
                });
            }
            searchItems.clear();
        } else {
            final List<Inventory> inventoryList = InventoryService.findAll(role);
            if (Objects.nonNull(inventoryList)) {
                for (Inventory inventory : inventoryList) {
                    model.addRow(new Object[]{
                            null,
                            inventory.getIdInventory(),
                            inventory.getRoom().getRoomNumber(),
                            inventory.getBranch().getName(),
                            inventory.getDescription(),
                            inventory.getActive()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao carregar dados do inventário.");
            }
        }
    }
}
