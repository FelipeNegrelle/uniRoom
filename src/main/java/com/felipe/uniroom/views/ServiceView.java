package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Service;
import com.felipe.uniroom.repositories.ServiceRepository;
import com.felipe.uniroom.services.ServiceService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Service> searchServices = new ArrayList<>();

    public ServiceView(Role role) {
        super(Constants.SERVICE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);
        setIconImage(Constants.LOGO);

        final JLabel titleLabel = new JLabel(Constants.SERVICE);
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
            new ServiceForm(role, null);
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
                searchServices.clear();
                searchServices.addAll(ServiceService.search(searchField.getText(), null, role));
                updateServiceTable(role);
            }
        });
        searchPanel.add(searchField, "align left");

        panel.add(searchPanel, "growx");

        model = new DefaultTableModel(new Object[]{Constants.ACTIONS, "Código", "Descrição", "Preço", Constants.BRANCH}, 0);

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
                    final Service service = ServiceService.findById((Integer) model.getValueAt(row, 1));
                    new ServiceForm(role, service);
                    dispose();
                });

                final JMenuItem deleteItem = new JMenuItem(Constants.DELETE);
                deleteItem.setIcon(Constants.DELETE_ICON);
                deleteItem.setFont(Constants.FONT.deriveFont(Font.BOLD));
                deleteItem.addActionListener(e -> {
                    final Service service = new Service();
                    service.setIdService((Integer) model.getValueAt(row, 1));
                    if (ServiceService.delete(service)) {
                        updateServiceTable(role);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar serviço.", "Erro", JOptionPane.ERROR_MESSAGE);
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

        updateServiceTable(role);

        add(panel, "grow");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void updateServiceTable(Role role) {
        model.setRowCount(0);

        if (!searchServices.isEmpty()) {
            for (Service service : searchServices) {
                model.addRow(new Object[]{
                        null,
                        service.getIdService(),
                        service.getDescription(),
                        "R$" + service.getPrice(),
                        service.getBranch().getName(),
                });
            }
            searchServices.clear();
        } else {
            final List<Service> serviceList = ServiceService.findAll(role);
            if (Objects.nonNull(serviceList)) {
                for (Service service : serviceList) {
                    model.addRow(new Object[]{
                            null,
                            service.getIdService(),
                            service.getDescription(),
                            "R$" + service.getPrice(),
                            service.getBranch().getName(),
                            service.getActive(),
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Falha ao carregar dados do serviço.");
            }
        }
    }
}
