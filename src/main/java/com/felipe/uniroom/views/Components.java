package com.felipe.uniroom.views;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.config.Util;
import com.felipe.uniroom.entities.Reservation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class Components {
    public static class CheckoutDialog extends JDialog {
        CheckoutDialog(JFrame parent, Reservation reservation, Role role) {
            super(parent, Constants.CHECKOUT);

            setSize(800, 600);
            setLocationRelativeTo(parent);

            final JLabel titleLabel = new JLabel(Constants.CHECKOUT);
            titleLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 40));
            titleLabel.setForeground(Constants.BLUE);

            final JLabel roomLabel = new JLabel(Constants.ROOM + ": " + reservation.getRoom().getRoomNumber());
            roomLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel branchLabel = new JLabel(Constants.BRANCH + ": " + reservation.getBranch().getName());
            branchLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel checkInLabel = new JLabel(Constants.CHECKIN + ": " + Util.formatNullableDate(reservation.getDateTimeCheckIn(), "dd/MM/yyyy HH:mm"));
            checkInLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel checkOutLabel = new JLabel(Constants.CHECKOUT + ": " + Util.formatNullableDate(reservation.getDateTimeCheckOut(), "dd/MM/yyyy HH:mm"));
            checkOutLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel guestLabel = new JLabel(Constants.GUEST + ": " + reservation.getGuestList().get(0).getName());
            guestLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel statusLabel = new JLabel(Constants.STATUS + ": " + reservation.getStatus());
            statusLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JLabel totalLabel = new JLabel(Constants.TOTAL + ": " + 0);
            totalLabel.setFont(Constants.FONT.deriveFont(Font.BOLD, 20));

            final JButton checkoutButton = new JButton(Constants.CHECKOUT);
            checkoutButton.setFont(Constants.FONT.deriveFont(Font.BOLD));

            final JButton backButton = new JButton(Constants.BACK);
            backButton.setFont(Constants.FONT.deriveFont(Font.BOLD));

            final JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", ""));
            panel.setBackground(Constants.WHITE);

            panel.add(titleLabel, "align center");
            panel.add(roomLabel, "align left");
            panel.add(branchLabel, "align left");
            panel.add(checkInLabel, "align left");
            panel.add(checkOutLabel, "align left");
            panel.add(guestLabel, "align left");
            panel.add(statusLabel, "align left");
            panel.add(totalLabel, "align left");
            panel.add(checkoutButton, "align center");
            panel.add(backButton, "align center");

            add(panel);
        }
    }

    public static class ForgetPasswordDialog extends JDialog {
        public ForgetPasswordDialog(JFrame parent, String user) {
            super(parent, Constants.FORGOT, true);
            setSize(750, 500);
            setLocationRelativeTo(parent);

            final JLabel nameLabel = new JLabel(Constants.USER + ": " + user);
            nameLabel.setFont(new Font("Sans", Font.BOLD, 20));

            final JLabel questionLabel = getLabel(Constants.SECRET_PHRASE, null, Font.BOLD, null, null);

            final JLabel answerLabel = getLabel(Constants.SECRET_ANSWER, null, Font.BOLD, null, null);

            final JTextField answerField = new JTextField(30);
            answerField.setFont(Constants.FONT);
            answerField.setPreferredSize(Constants.TEXT_FIELD_SIZE);

            JButton sendButton = new JButton(Constants.SEND);
            sendButton.setFont(Constants.FONT);
            sendButton.setPreferredSize(Constants.BUTTON_SIZE);
            sendButton.setBackground(Constants.BLUE);
            sendButton.setForeground(Color.white);

            JPanel panel = new JPanel(new MigLayout("fill", "[grow][fill]", "[]10[]"));
            panel.add(nameLabel, "alignx left, wrap");
            panel.add(questionLabel, "alignx left, wrap");
            panel.add(answerLabel, "alignx left, wrap");
            panel.add(answerField, "growx, wrap");
            panel.add(sendButton, "span 2, align center");

            sendButton.addActionListener(e -> {
//                if (Auth.checkSecretAnswer(user, answerField.getText().replaceAll(" ", "").toLowerCase())) {
//                    dispose();
//                    new ResetPasswordDialog(parent, user).setVisible(true);
//                    System.out.println(user);
//                } else {
//                    JOptionPane.showMessageDialog(ForgetPasswordDialog.this, "Resposta incorreta", "Erro", JOptionPane.ERROR_MESSAGE);
//                    System.out.println(answerField.getText().trim().toLowerCase());
//                }
                System.out.println(user);
            });

            add(panel);
        }
    }

    public static JLabel getLabel(String text, String fontFamily, Integer fontStyle, Integer fontSize, Color color) {
        final JLabel label = new JLabel(text);

        final Font font = new Font(
                !Objects.isNull(fontFamily) && !fontFamily.isEmpty() ? fontFamily : "Sans",
                !Objects.isNull(fontStyle) && fontStyle >= 0 ? fontStyle : Font.PLAIN,
                !Objects.isNull(fontSize) && fontSize > 0 ? fontSize : 20);
        label.setFont(font);

        if (Objects.nonNull(color)) {
            label.setForeground(color);
        } else {
            label.setForeground(Constants.BLACK);
        }

        return label;
    }

    public static JDialog getErrorDialog(JFrame parent, String message) {
        final JDialog dialog = new JDialog(parent, Constants.ERROR, true);

        dialog.setAlwaysOnTop(true);

        JOptionPane.showMessageDialog(dialog, message, Constants.ERROR, JOptionPane.ERROR_MESSAGE);

        return dialog;
    }

    public static class IconCellRenderer extends DefaultTableCellRenderer {
        private final Icon activeIcon;
        private final Icon inactiveIcon;

        public IconCellRenderer() {
            activeIcon = Constants.CHECKBOX_ICON;

            inactiveIcon = Constants.BOX_ICON;

            setHorizontalAlignment(SwingConstants.LEADING);
        }

        @Override
        protected void setValue(Object value) {
            if (value instanceof Boolean isActive) {
                setIcon(isActive ? activeIcon : inactiveIcon);
            } else {
                super.setValue(value);
            }
        }
    }

    public static class OptionsCellRenderer extends JLabel implements TableCellRenderer {

        private final Icon icon;

        public OptionsCellRenderer() {
            icon = Constants.MORE_ICON;

            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(icon);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }

    @FunctionalInterface
    public interface MouseAction {
        void execute(JTable table, MouseEvent evt);
    }

    public static class GenericMouseListener extends MouseAdapter {
        private final JTable table;
        private final int column;
        private final MouseAction action;

        public GenericMouseListener(JTable table, int column, MouseAction action) {
            this.table = table;
            this.column = column;
            this.action = action;
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            final int col = table.columnAtPoint(evt.getPoint());

            if (col == column) {
                action.execute(table, evt);
            }
        }
    }

    public static void showOptionsMenu(JTable table, List<JMenuItem> itens, MouseEvent evt) {
        final JPopupMenu menu = new JPopupMenu();

        itens.forEach(menu::add);

        menu.show(table, evt.getX(), evt.getY());
    }


    public static MaskFormatter getCnpjFormatter() {
        MaskFormatter cnpjMask;
        try {
            cnpjMask = new MaskFormatter("##.###.###/####-##");
            cnpjMask.setPlaceholderCharacter('_');

            return cnpjMask;
        } catch (
                ParseException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static MaskFormatter getCpfFormatter() {
        MaskFormatter cpfMask;
        try {
            cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');

            return cpfMask;
        } catch (
                ParseException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void showGenericError(JFrame parent) {
        JOptionPane.showMessageDialog(parent, Constants.GENERIC_ERROR, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
