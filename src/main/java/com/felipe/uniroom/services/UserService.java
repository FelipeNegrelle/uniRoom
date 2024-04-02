package com.felipe.uniroom.services;

import com.felipe.uniroom.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.DatabaseRepository;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserService {
    private static final UserRepository userRepository = new UserRepository();

    public static Boolean register(User user) throws Exception {
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new Exception("Usuário " + user.getUsername() + " já existe, escolha um novo nome.");
        }

        user.setPassword(hashedPassword);

        System.out.println(user);// todo tirar depois

        return userRepository.saveOrUpdate(user);
    }

    public static Boolean login(User user) {
        try {
            final User result = userRepository.findByUsername(user.getUsername());

            if (result != null) {
                return BCrypt.checkpw(user.getPassword(), result.getPassword());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static Boolean update(User user) {
        try {
            final User result = userRepository.findById(User.class, user.getIdUser());

            if (result != null) {
                final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

                user.setIdUser(result.getIdUser());
                user.setName(result.getName());
                user.setUsername(result.getUsername());
                user.setPassword(hashedPassword);
                user.setRole(result.getRole());
                user.setSecretPhrase(result.getSecretPhrase());
                user.setSecretAnswer(result.getSecretAnswer());
                user.setActive(result.getActive());
                user.setCorporate(result.getCorporate());

                return userRepository.saveOrUpdate(user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static Boolean checkSecretAnswer(User user) {
        try {
            final User result = userRepository.findByUsername(user.getUsername());

            if (result != null) {
                return user.getSecretAnswer().equals(result.getSecretAnswer());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static Boolean forgotPassword(User user) {
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try {
            final User result = userRepository.findByUsername(user.getUsername());

            if (result != null) {
                user.setIdUser(result.getIdUser());
                user.setPassword(hashedPassword);

                return userRepository.saveOrUpdate(user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
