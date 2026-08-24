package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Inventory;
import com.felipe.uniroom.entities.InventoryItem;
import com.felipe.uniroom.entities.InventoryItemId;
import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.InventoryItemRepository;
import com.felipe.uniroom.services.InventoryItemService;
import com.felipe.uniroom.services.InventoryService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class
InventoryItemView extends JFrame {
    private static DefaultTableModel model;
    private static final List<InventoryItem> searchItems = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();

    public InventoryItemView(Role role, Inventory inventory) {
        super("Itens do inventário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final List<Item> l = InventoryService.getItemsFromInventory(inventory);
        if (Objects.nonNull(l)) {
            items.addAll(l);
        }

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel("Itens do inventário");
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
            new InventoryView(role, inventory.getRoom());
            dispose();
        });
        searchPanel.add(backButton, "align left");

        final JButton newInventory = new JButton(Constants.NEW);
        newInventory.setBackground(Constants.WHITE);
        newInventory.setForeground(Constants.BLACK);
        newInventory.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newInventory.addActionListener(e -> {
            new InventoryItemForm(role, inventory, null);
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
                searchItems.addAll(InventoryItemService.search(searchField.getText(), null, role));
                updateInventoryItemTable(inventory, role);
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código do Inventário", "Código do Item", Constants.ITEM, Constants.QUANTITY}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final Components.OptionsCellRenderer optionsCellRenderer = new Components.OptionsCellRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(optionsCellRenderer);

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final InventoryItem inventoryItem = InventoryItemRepository.findById(InventoryItem.class, new InventoryItemId((int) model.getValueAt(row, 1), (int) model.getValueAt(row, 2)));
                    new InventoryItemForm(role, inventory, inventoryItem);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final InventoryItem inventoryItem = new InventoryItem();
                    inventoryItem.setId((InventoryItemId) model.getValueAt(row, 1));//todo ver como pegar id
                    if (InventoryItemService.delete(inventoryItem)) {
                        updateInventoryItemTable(inventory, role);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar item", "Error", JOptionPane.ERROR_MESSAGE);
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

        updateInventoryItemTable(inventory, role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateInventoryItemTable(Inventory inventory, Role role) {
        model.setRowCount(0);

        if (!searchItems.isEmpty()) {
            for (InventoryItem inventoryItem : searchItems) {
                final Item item = findItemById(inventoryItem.getId().getIdItem());
                if (item != null) {
                    model.addRow(new Object[]{
                            null,
                            inventoryItem.getId().getIdInvetory(),
                            inventoryItem.getId().getIdItem(),
                            item.getName(),
                            inventoryItem.getQuantity(),
                    });
                }
            }
            searchItems.clear();
        } else {
            final List<InventoryItem> inventoryItemList = InventoryItemService.findAll(inventory, role);
            if (Objects.nonNull(inventoryItemList)) {
                for (InventoryItem inventoryItem : inventoryItemList) {
                    final Item item = findItemById(inventoryItem.getId().getIdItem());
                    if (item != null) {
                        model.addRow(new Object[]{
                                null,
                                inventoryItem.getId().getIdInvetory(),
                                inventoryItem.getId().getIdItem(),
                                item.getName(),
                                inventoryItem.getQuantity(),
                        });
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao carregar dados do inventário.");
            }
        }
    }

    private static Item findItemById(int id) {
        for (Item item : items) {
            if (item.getIdItem() == id) {
                return item;
            }
        }
        return null;
    }
}
