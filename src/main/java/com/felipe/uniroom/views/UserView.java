package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.services.UserService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;

public class UserView extends JFrame {
    private static DefaultTableModel model;

    public UserView(Role role) {
        super(Constants.USER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.USER);
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

        final JButton newUser = new JButton(Constants.NEW);
        newUser.setBackground(Constants.WHITE);
        newUser.setForeground(Constants.BLACK);
        newUser.setFont(Constants.FONT.deriveFont(Font.BOLD));
        newUser.addActionListener(e -> {
            new UserForm(role, null);
            dispose();
        });
        searchPanel.add(newUser, "align left");

        final JLabel searchLabel = new JLabel(Constants.SEARCH);
        searchLabel.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setPreferredSize(new Dimension(70, 40));
        searchPanel.add(searchLabel, "align right");

        final JTextField searchField = new JTextField(20);
        searchField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchField.setPreferredSize(new Dimension(200, 40));
        searchPanel.add(searchField, "align left");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchField.getText().trim().toLowerCase().replaceAll(" ", "");
                updateUserTable(searchText, role);
            }
        });

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", Constants.NAME, "Nome de usuário", "Cargo", Constants.CORPORATE, Constants.BRANCH}, 0);

        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD));

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
                    final User user = UserService.findById((int) model.getValueAt(row, 1));
                    new UserForm(role, user);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final User user = UserService.findById((int) model.getValueAt(row, 1));

                    if (UserService.delete(user)) {
                        updateUserTable(searchField.getText(), role);
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

        updateUserTable("", role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static void updateUserTable(String searchText, Role role) {
        model.setRowCount(0);

        if (!searchText.isEmpty()) {
            final List<User> searchItens = UserService.search(searchText, role);

            for (User user : searchItens) {
                model.addRow(new Object[]{
                        null,
                        user.getIdUser(),
                        user.getName(),
                        user.getUsername(),
                        Util.convertRoleName(user.getRole()),
                        Objects.isNull(user.getCorporate()) ? "-" : user.getCorporate().getName(),
                        Objects.isNull(user.getBranch()) ? "-" : user.getBranch().getName(),
                });
            }

            searchItens.clear();
        } else {
            final List<User> userList = UserService.findAll(role);

            if (Objects.nonNull(userList)) {
                for (User user : userList) {
                    model.addRow(new Object[]{
                            null,
                            user.getIdUser(),
                            user.getName(),
                            user.getUsername(),
                            Util.convertRoleName(user.getRole()),
                            Objects.isNull(user.getCorporate()) ? "-" : user.getCorporate().getName(),
                            Objects.isNull(user.getBranch()) ? "-" : user.getBranch().getName(),
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
            }
        }
    }
}