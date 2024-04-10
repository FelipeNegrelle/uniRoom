package com.felipe.uniroom;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.UserService;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.invokeLater(com.felipe.uniroom.view.Login::new);

            final User admin = new User();
            admin.setName("Administrator");
            admin.setUsername("admin");
            admin.setRole('A');
            admin.setSecretPhrase("Why is my cock hard?");
            admin.setSecretAnswer("because yes");
            admin.setPassword("@123Mudar");
            admin.setActive(true);

            if (Objects.isNull(UserRepository.findByUsername(admin.getUsername()))) {
                UserService.save(admin);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(ConnectionManager::closeEntityManagerFactory));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}