package com.felipe.uniroom.services;

import org.mindrot.jbcrypt.BCrypt;

import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.DatabaseRepository;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class UserService {
    private static DatabaseRepository db = new DatabaseRepository();

    public static Boolean register(User user) {

        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try {
            if (db.findByUsername(user.getUsername()) != null) {
                final JDialog dialog = new JDialog();

                dialog.setAlwaysOnTop(true);

                JOptionPane.showMessageDialog(dialog,
                        "Usuário " + user.getUsername() + " já existe, escolha um novo nome.");

                return false;
            }

            user.setPassword(hashedPassword);

            System.out.println(user.toString());// todo tirar depois

            final boolean result = db.saveOrUpdate(user);

            if (result) {
                final JDialog dialog = new JDialog();

                dialog.setAlwaysOnTop(true);

                JOptionPane.showMessageDialog(dialog, "Usuário " + user.getUsername() + " cadastrado com sucesso.");
            } else {
                final JDialog dialog = new JDialog();

                dialog.setAlwaysOnTop(true);

                JOptionPane.showMessageDialog(dialog, "Erro ao cadastrar usuário " + user.getUsername() + ".");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }

    public static Boolean login(User user) {
        try {
            final User result = db.findByUsername(user.getUsername());

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
            final User result = db.findById(User.class, user.getIdUser());

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

                db.saveOrUpdate(user);

                return true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }

    public static Boolean checkSecretAnswer(User user) {
        try {
            final User result = db.findByUsername(user.getUsername());

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
            final User result = db.findByUsername(user.getUsername());

            if (result != null) {
                user.setIdUser(result.getIdUser());
                user.setPassword(hashedPassword);

                db.saveOrUpdate(user);

                return true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }
}
