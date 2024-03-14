package com.felipe.uniroom;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.felipe.uniroom.controller.ConnectionManager;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        SwingUtilities.invokeLater(com.felipe.uniroom.view.Login::new);

        ConnectionManager.getEntityManager().close();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConnectionManager.close();
        }));
    }
}