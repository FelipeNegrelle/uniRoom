package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.services.RoomService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class RoomView extends JFrame {

    private static DefaultTableModel model;
    private static boolean filterApplied = false;

    public RoomView(Role role) {
        super(Constants.ROOM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.ROOM);
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

        final JButton newRoom = new JButton(Constants.NEW);
        newRoom.setBackground(Constants.WHITE);
        newRoom.setForeground(Constants.BLACK);
        newRoom.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newRoom.addActionListener(e -> {
            new RoomForm(role, null);
            dispose();
        });
        searchPanel.add(newRoom, "align left");

        final JLabel isHostedLabel = Components.getLabel("Ocupado?", null, Font.BOLD, null, Constants.WHITE);
        searchPanel.add(isHostedLabel, "align right");

        final JComboBox<String> isHostedComboBox = new JComboBox<>(new String[]{"Sim", "Não"});
        isHostedComboBox.setFont(Constants.FONT.deriveFont(Font.BOLD));
        isHostedComboBox.setBackground(Constants.WHITE);
        searchPanel.add(isHostedComboBox, "align right");

        final JButton filterButton = new JButton("Filtrar");
        filterButton.setBackground(Constants.WHITE);
        filterButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
        filterButton.addActionListener(e1 -> {
            filterApplied = true;
            updateRoomTable(isHostedComboBox, role);
        });
        searchPanel.add(filterButton, "align left");

        final JButton clearFilter = new JButton("Limpar filtro");
        clearFilter.setBackground(Constants.WHITE);
        clearFilter.setFont(Constants.FONT.deriveFont(Font.BOLD));
        clearFilter.addActionListener(e -> {
            filterApplied = false;
            updateRoomTable(isHostedComboBox, role);
        });
        searchPanel.add(clearFilter, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nº quarto", "Tipo", "Preço", "Capacidade", Constants.BRANCH, "Ocupado"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final Components.IconCellRenderer iconCellRenderer = new Components.IconCellRenderer();
        final TableColumn activeColumn = table.getColumnModel().getColumn(7);
        activeColumn.setCellRenderer(iconCellRenderer);

        final Components.OptionsCellRenderer optionsCellRenderer = new Components.OptionsCellRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(optionsCellRenderer);

        final Components.MouseAction mouseAction = (tableEvt, evt) -> {
            final int row = tableEvt.rowAtPoint(evt.getPoint());
            final int column = tableEvt.columnAtPoint(evt.getPoint());

            if (column == 0) {
                final JPopupMenu popupMenu = new JPopupMenu();

                final JMenuItem inventoryItem = new JMenuItem(Constants.INVENTORY);
                inventoryItem.setIcon(Constants.INVENTORY_ICON);
                inventoryItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                inventoryItem.addActionListener(e -> {
                    final Room room = RoomService.findById((Integer) model.getValueAt(row, 1));
                    new InventoryView(role, room);
                    dispose();
                });

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Room Room = RoomService.findById((Integer) model.getValueAt(row, 1));
                    new RoomForm(role, Room);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Room Room = new Room();
                    Room.setIdRoom((Integer) model.getValueAt(row, 1));
                    if (RoomService.delete(Room)) {
                        updateRoomTable(isHostedComboBox, role);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar quarto", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                popupMenu.add(inventoryItem);
                popupMenu.add(editItem);
                popupMenu.add(deleteItem);

                popupMenu.show(tableEvt, evt.getX(), evt.getY());
            }
        };
        final Components.GenericMouseListener genericMouseListener = new Components.GenericMouseListener(table, 0, mouseAction);

        table.addMouseListener(genericMouseListener);

        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow");

        updateRoomTable(isHostedComboBox, role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateRoomTable(JComboBox<String> isHostedComboBox, Role role) {
        model.setRowCount(0);

        if (filterApplied) {
            final boolean isOccupied = isHostedComboBox.getSelectedItem().equals("Sim");

            final List<Room> filteredRooms = RoomService.getRoomByStatus(isOccupied, role);
            if (Objects.nonNull(filteredRooms)) {
                for (Room room : filteredRooms) {
                    model.addRow(new Object[]{
                            null,
                            room.getIdRoom(),
                            room.getRoomNumber(),
                            room.getRoomType().getName(),
                            "R$ " + room.getRoomType().getPrice(),
                            room.getRoomType().getCapacity(),
                            room.getBranch().getName(),
                            RoomService.isRoomOccupied(room)
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao carregar os dados do quarto");
            }
        } else {
            final List<Room> RoomList = RoomService.findAll(role);
            if (Objects.nonNull(RoomList)) {
                for (Room room : RoomList) {
                    model.addRow(new Object[]{
                            null,
                            room.getIdRoom(),
                            room.getRoomNumber(),
                            room.getRoomType().getName(),
                            "R$ " + room.getRoomType().getPrice(),
                            room.getRoomType().getCapacity(),
                            room.getBranch().getName(),
                            RoomService.isRoomOccupied(room)
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao carregar os dados do quarto");
            }
        }
    }
}
