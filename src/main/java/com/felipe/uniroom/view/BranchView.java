package com.felipe.uniroom.view;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.services.BranchService;
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

public class BranchView extends JFrame {
    private static DefaultTableModel model;
    private static final List<Branch> searchItens = new ArrayList<>();

    public BranchView(Role role) {
        super(Constants.BRANCH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("fill, insets 0"));

        // Painel principal
        final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
        panel.setBackground(Constants.BLUE);

        // Título
        final JLabel titleLabel = new JLabel(Constants.BRANCH);
        titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, "align center"); // Reduz o gap abaixo do título

        // Painel de pesquisa
        final JPanel searchPanel = new JPanel(new MigLayout("insets 0", "[right]unrel[grow]unrel[]", ""));
        searchPanel.setBackground(Constants.BLUE);

        // Rótulo de pesquisa
        final JLabel searchLabel = new JLabel(Constants.SEARCH);
        searchLabel.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setPreferredSize(new Dimension(70, 40)); // Altura de 40 pixels
        searchPanel.add(searchLabel);

        // Campo de pesquisa
        final JTextField searchField = new JTextField(20);
        searchField.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchField.setPreferredSize(new Dimension(200, 40)); // Altura de 40 pixels
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchItens.addAll(BranchService.search(searchField.getText(), null));
                updateBranchTable();
            }
        });
        searchPanel.add(searchField, "growx");

        // Botão de pesquisa
        final JButton searchButton = new JButton(Constants.SEARCH);
        searchButton.setBackground(Constants.WHITE);
        searchButton.setForeground(Constants.BLACK);
        searchButton.setFont(Constants.FONT.deriveFont(Font.BOLD));
        searchButton.setPreferredSize(new Dimension(100, 40)); // Altura de 40 pixels
        searchButton.addActionListener(e -> {
            searchItens.addAll(BranchService.search(searchField.getText(), null));
            updateBranchTable();
        });
        searchPanel.add(searchButton, "");

        // Adiciona o painel de pesquisa à direita do painel principal
        panel.add(searchPanel, "align right");

        // Modelo da tabela
        model = new DefaultTableModel(new Object[]{"Código", "Nome", "CNPJ", "Matriz", "Gerente", "Ativo"}, 0);

        // Tabela
        final JTable table = new JTable(model);
        table.setFont(new Font("Sans", Font.PLAIN, 20));
        table.setSelectionBackground(Constants.BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

        // Configura o renderizador da coluna "Ativo"
        final Components.IconCellRenderer iconCellRenderer = new Components.IconCellRenderer();
        final TableColumn activeColumn = table.getColumnModel().getColumn(5);
        activeColumn.setCellRenderer(iconCellRenderer);

        // ScrollPane para a tabela
        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow"); // Permite que a tabela cresça para ocupar o espaço disponível

        // Botões
        final JButton returnButton = new JButton(Constants.BACK);
        returnButton.addActionListener(e -> {
            new Home(role);
            dispose();
        });
        returnButton.setBackground(Constants.RED);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));

        final JButton newBranch = new JButton(Constants.NEW);
        newBranch.setBackground(Constants.WHITE);
        newBranch.setForeground(Constants.BLACK);
        newBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        newBranch.addActionListener(e -> {
            new BranchForm(role, null);
            dispose();
        });

        final JButton editBranch = new JButton(Constants.EDIT);
        editBranch.setBackground(Constants.WHITE);
        editBranch.setForeground(Constants.BLACK);
        editBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        editBranch.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                final Branch branch = BranchRepository.findById(Branch.class, (int) model.getValueAt(selectedRow, 0));

                new BranchForm(role, branch);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, Constants.EDIT_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        final JButton deleteBranch = new JButton(Constants.DELETE);
        deleteBranch.setBackground(Constants.RED);
        deleteBranch.setForeground(Color.WHITE);
        deleteBranch.setFont(Constants.FONT.deriveFont(Font.BOLD, 50));
        deleteBranch.addActionListener(e -> {
            final int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                final Branch branch = new Branch();

                branch.setIdBranch((int) model.getValueAt(selectedRow, 0));

                if (BranchService.delete(branch)) {
                    updateBranchTable();
                } else {
                    Components.showGenericError(this);
                }
            } else {
                JOptionPane.showMessageDialog(this, Constants.DELETE_WARN, Constants.WARN, JOptionPane.WARNING_MESSAGE);
            }
        });

        updateBranchTable();

        // Adiciona os botões ao final do painel principal
        panel.add(returnButton, "split 4, align left, growx");
        panel.add(newBranch, "align right, growx");
        panel.add(editBranch, "align center, growx");
        panel.add(deleteBranch, "align right, growx");

        add(panel, "grow");
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void updateBranchTable() {
        model.setRowCount(0);

        if (!searchItens.isEmpty()) {
            for (Branch branch : searchItens) {
                model.addRow(new Object[]{
                        branch.getIdBranch(),
                        branch.getName(),
                        Util.formatCnpj(branch.getCnpj()),
                        branch.getCorporate().getName(),
                        branch.getUser().getName(),
                        branch.getActive(),
                });
            }
            searchItens.clear();
        } else {
            final List<Branch> branchList = BranchRepository.findAll(Branch.class);
            if (Objects.nonNull(branchList)) {
                for (Branch branch : branchList) {
                    model.addRow(new Object[]{
                            branch.getIdBranch(),
                            branch.getName(),
                            Util.formatCnpj(branch.getCnpj()),
                            branch.getCorporate().getName(),
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