package com.felipe.uniroom.services;

import com.felipe.uniroom.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.DatabaseRepository;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserService {
    public static Boolean register(User user) throws Exception {
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        if (UserRepository.findByUsername(user.getUsername()) != null) {
            throw new Exception("Usuário " + user.getUsername() + " já existe, escolha um novo nome.");
        }

        user.setPassword(hashedPassword);

        System.out.println(user);// todo tirar depois

        return UserRepository.saveOrUpdate(user);
    }

    public static Boolean login(User user) {
        try {
            final User result = UserRepository.findByUsername(user.getUsername());

            if (result != null) {
                final String teste = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                System.out.println(teste);

                System.out.println(result.getPassword());
                System.out.println(teste == result.getPassword());
                return BCrypt.checkpw(user.getPassword(), result.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean save(User user) {
        try {
            final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            user.setPassword(hashedPassword);

            return UserRepository.saveOrUpdate(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean update(User user) {
        try {
            final User result = UserRepository.findById(User.class, user.getIdUser());

            if (result != null) {
                result.setName(user.getName());
                result.setUsername(user.getUsername());
                result.setRole(user.getRole());
                result.setSecretPhrase(user.getSecretPhrase());
                result.setSecretAnswer(user.getSecretAnswer());
                result.setActive(user.getActive());

                String newPassword = user.getPassword();
                if (newPassword != null && !newPassword.isEmpty()) {
                    result.setPassword(newPassword);
                }

                return UserRepository.saveOrUpdate(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean checkSecretAnswer(User user) {
        try {
            final User result = UserRepository.findByUsername(user.getUsername());

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
            final User result = UserRepository.findByUsername(user.getUsername());

            if (result != null) {
                user.setIdUser(result.getIdUser());
                user.setPassword(hashedPassword);

                return UserRepository.saveOrUpdate(user);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
