package com.felipe.uniroom;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.felipe.uniroom.config.ConnectionManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.invokeLater(com.felipe.uniroom.view.Home::new);

            ConnectionManager.getEntityManager().close();

            Runtime.getRuntime().addShutdownHook(new Thread(ConnectionManager::close));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}