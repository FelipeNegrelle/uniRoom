package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Room;
import com.felipe.uniroom.repositories.RoomRepository;
import com.felipe.uniroom.services.RoomService;
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

public class RoomView extends JFrame{

    private static DefaultTableModel model;
    private static final List<Room> searchItems = new ArrayList<>();

    public RoomView(Role role){
        super(Constants.ROOM);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);

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
                searchItems.addAll(RoomService.search(searchField.getText(), null));
                updateRoomTable();
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nº quarto", "Tipo","Preço", "Capacidade", "Filial", "Ativo"}, 0);

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

                final JMenuItem editItem = new JMenuItem(Constants.EDIT);
                editItem.setIcon(Constants.EDIT_ICON);
                editItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                editItem.addActionListener(e -> {
                    final Room Room = RoomRepository.findById(Room.class, (Integer) model.getValueAt(row, 1));
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
                        updateRoomTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar quarto", "Error", JOptionPane.ERROR_MESSAGE);
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

        updateRoomTable();

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
    }

    private static void updateRoomTable() {
        model.setRowCount(0);

        if (!searchItems.isEmpty()) {
            for (Room room : searchItems) {
                model.addRow(new Object[]{
                        null,
                        room.getIdRoom(),
                        room.getRoomNumber(),
                        room.getRoomType().getName(),
                        "R$ " +room.getRoomType().getPrice(),
                        room.getRoomType().getCapacity(),
                        room.getBranch().getName(),
                        room.getActive()
                });
            }
            searchItems.clear();
        } else {
            final List<Room> RoomList = RoomRepository.findAll(Room.class);
            if (Objects.nonNull(RoomList)) {
                for (Room room : RoomList) {
                    model.addRow(new Object[]{
                            null,
                            room.getIdRoom(),
                            room.getRoomNumber(),
                            room.getRoomType().getName(),
                            "R$ " +room.getRoomType().getPrice(),
                            room.getRoomType().getCapacity(),
                            room.getBranch().getName(),
                            room.getActive()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load room type data");
            }
        }
    }
}
