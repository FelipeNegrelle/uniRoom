package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Guest;
import com.felipe.uniroom.services.GuestService;
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

public class GuestView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Guest> searchItens = new ArrayList<>();

    public GuestView(Role role) {
        super(Constants.GUEST);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);
        final JLabel titleLabel = new JLabel(Constants.GUEST);
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

        final JButton newGuest = new JButton(Constants.NEW);
        newGuest.setBackground(Constants.WHITE);
        newGuest.setForeground(Constants.BLACK);
        newGuest.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newGuest.addActionListener(e -> {
            new GuestForm(role, null);
            dispose();
        });
        searchPanel.add(newGuest, "align left");

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
                searchItens.addAll(GuestService.search(searchField.getText(), null, role));
                updateGuestTable(role);
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nome", "CPF", "Passaporte", "Hospedado"}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        final Components.IconCellRenderer iconCellRenderer = new Components.IconCellRenderer();
        final TableColumn hostedColumn = table.getColumnModel().getColumn(5);
        hostedColumn.setCellRenderer(iconCellRenderer);

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
                    final Guest guest = GuestService.findById((int) model.getValueAt(row, 1));

                    new GuestForm(role, guest);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Guest guest = new Guest();

                    guest.setIdGuest((int) model.getValueAt(row, 1));

                    if (GuestService.delete(guest)) {
                        updateGuestTable(role);
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

        updateGuestTable(role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateGuestTable(Role role) {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Guest guest : searchItens) {
                model.addRow(new Object[]{
                        null,
                        guest.getIdGuest(),
                        guest.getName(),
                        guest.getCpf() != null ? Util.maskCpf(guest.getCpf()) : "-",
                        guest.getPassportNumber() != null ? guest.getPassportNumber() : "-",
                        guest.isHosted()
                });
            }
            searchItens.clear();
        } else {
            final List<Guest> guestList = GuestService.findAll(role);
            if (Objects.nonNull(guestList)) {
                for (Guest guest : guestList) {
                    model.addRow(new Object[]{
                            null,
                            guest.getIdGuest(),
                            guest.getName(),
                            guest.getCpf() != null ? Util.maskCpf(guest.getCpf()) : "-",
                            guest.getPassportNumber() != null ? guest.getPassportNumber() : "-",
                            guest.isHosted()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }
}
