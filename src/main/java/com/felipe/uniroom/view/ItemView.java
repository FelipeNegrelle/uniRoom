package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Item;
import com.felipe.uniroom.repositories.ItemRepository;
import com.felipe.uniroom.services.ItemService;
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

public class ItemView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Item> searchItems = new ArrayList<>();

    public ItemView(Role role) {
        super(Constants.ITEM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);

        final JLabel titleLabel = new JLabel(Constants.ITEM);
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

        final JButton newItem = new JButton(Constants.NEW);
        newItem.setBackground(Constants.WHITE);
        newItem.setForeground(Constants.BLACK);
        newItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newItem.addActionListener(e -> {
            new ItemForm(role, null);
            dispose();
        });
        searchPanel.add(newItem, "align left");

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
                searchItems.addAll(ItemService.search(searchField.getText(), null));
                updateItemTable();
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nome", "Preço", "Filial", "Ativo"}, 0);

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

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Item item = ItemRepository.findById(Item.class, (Integer) model.getValueAt(row, 1));
                    new ItemForm(role, item);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Item item = new Item();
                    item.setIdItem((Integer) model.getValueAt(row, 1));
                    if (ItemService.delete(item)) {
                        updateItemTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar item.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        updateItemTable();

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
    }

    private void updateItemTable() {
        model.setRowCount(0);

        if (!searchItems.isEmpty()) {
            for (Item item : searchItems) {
                model.addRow(new Object[]{
                        null,
                        item.getIdItem(),
                        item.getName(),
                        "R$" + item.getPrice(),
                        item.getBranch().getName(),
                        item.getActive()
                });
            }
            searchItems.clear();
        } else {
            final List<Item> itemList = ItemRepository.findAll(Item.class);
            if (Objects.nonNull(itemList)) {
                for (Item item : itemList) {
                    model.addRow(new Object[]{
                            null,
                            item.getIdItem(),
                            item.getName(),
                            "R$" + item.getPrice(),
                            item.getBranch().getName(),
                            item.getActive(),
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load item data");
            }
        }
    }
}
