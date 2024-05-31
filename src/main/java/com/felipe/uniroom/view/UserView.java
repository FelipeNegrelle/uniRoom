package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.UserRepository;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
                String searchText = searchField.getText().trim().toLowerCase();
                updateUserTable(searchText);
            }
        });

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Nome", "Username", "Cargo", "Ativo"}, 0);

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
                    final User user = UserRepository.findById(User.class, (int) model.getValueAt(row, 1));

                    new UserForm(role, user);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final User user = UserRepository.findById(User.class, (int) model.getValueAt(row, 1));

                    if (UserRepository.delete(User.class, user.getIdUser())) {
                        updateUserTable(searchField.getText());
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

        updateUserTable("");

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setVisible(true);
    }

    private static String getRoleTranslation(char roleCode) {
        return switch (roleCode) {
            case 'A' ->
                    "Administrador";
            case 'C' ->
                    "Gerente de Matriz";
            case 'B' ->
                    "Gerente de Filial";
            case 'E' ->
                    "Funcionário";
            default ->
                    "Desconhecido";
        };
    }

    private static void updateUserTable(String searchText) {
        model.setRowCount(0);

        final List<User> userList = UserRepository.searchByUsernameOrName(searchText);
        if (Objects.nonNull(userList)) {
            for (User user : userList) {
                model.addRow(new Object[]{
                        "...", // Adiciona os três pontinhos como uma opção de edição e exclusão
                        user.getIdUser(),
                        user.getName(),
                        user.getUsername(),
                        getRoleTranslation(user.getRole()),
                        user.getActive()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, Constants.GENERIC_ERROR);
        }
    }
}