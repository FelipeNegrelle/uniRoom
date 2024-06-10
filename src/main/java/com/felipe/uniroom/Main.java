package com.felipe.uniroom;

import com.felipe.uniroom.config.ConnectionManager;
import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.CorporateRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.services.CorporateService;
import com.felipe.uniroom.services.UserService;

import javax.swing.*;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.invokeLater(com.felipe.uniroom.views.Login::new);

            final User admin = new User();
            admin.setName("Administrator");
            admin.setUsername("admin");
            admin.setRole('A');
            admin.setSecretPhrase("Why is my cock hard?");
            admin.setSecretAnswer("because yes");
            admin.setPassword("senha");
            admin.setActive(true);

            final Corporate uniroom = new Corporate();
            uniroom.setName(Constants.UNIROOM);
            uniroom.setCnpj("24786224000139");
            uniroom.setUser(UserRepository.findByUsername(admin.getUsername()));
            uniroom.setActive(true);

            if (Objects.isNull(UserRepository.findByUsername(admin.getUsername()))) {
                System.out.println("user is null");
                UserService.register(admin);
            }

            if (Objects.isNull(CorporateRepository.findByCnpj(uniroom.getCnpj()))) {
                System.out.println("corporate is null");
                CorporateService.save(uniroom);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(ConnectionManager::closeEntityManagerFactory));
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}