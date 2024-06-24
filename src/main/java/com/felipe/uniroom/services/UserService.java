package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.config.Role;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.entities.Corporate;
import com.felipe.uniroom.entities.User;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.views.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserService {
    private static final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    public static String validateUser(User user, boolean isUpdate) {
        final StringBuilder errorsSb = new StringBuilder();

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            violations.forEach(violation -> errorsSb.append(violation.getMessage()).append("\n"));
        }


        if (user.getUsername().isBlank()) {
            errorsSb.append("Nome de usuário não pode ser vazio!\n");
        }

        if (isUpdate) {
            User existingUser = UserRepository.findByUsername(user.getUsername());

            if (existingUser != null && !existingUser.getIdUser().equals(user.getIdUser())) {
                errorsSb.append("Nome de usuário já cadastrado!\n");
            }
        } else {
            if (UserRepository.findByUsername(user.getUsername()) != null) {
                errorsSb.append("Nome de usuário já cadastrado!\n");
            }
        }

        if (user.getName().isBlank()) {
            errorsSb.append("Nome do usuário não pode ser vazio!\n");
        }

        if (user.getName().length() > 50) {
            errorsSb.append("Nome do usuário deve ter no máximo 50 caracteres!\n");
        }

        if (Objects.isNull(user.getRole())) {
            errorsSb.append("Cargo não pode ser vazio!\n");
        }

        if (Objects.isNull(user.getSecretPhrase()) || user.getSecretPhrase().isBlank()) {
            errorsSb.append("Frase secreta não pode ser vazia!\n");
        }

        if (Objects.isNull(user.getSecretAnswer()) || user.getSecretAnswer().isBlank()) {
            errorsSb.append("Resposta secreta não pode ser vazia!\n");
        }

        return errorsSb.toString();
    }

    public static List<User> findAll(Role role){
        return UserRepository.findAll(User.class, role);
    }

    public static User findById(int id){
        return UserRepository.findById(User.class,id);
    }

    public static Boolean register(User user) {
        try {

            final String validations = validateUser(user, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                user.setPassword(hashedPassword);
                user.setSecretAnswer(user.getSecretAnswer().trim().toLowerCase().replace(" ", ""));
                return UserRepository.saveOrUpdate(user);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return null;
        }
    }

    public static Boolean login(User user) {
        try {
            final User result = UserRepository.findByUsername(user.getUsername());

            if (result != null) {
                return BCrypt.checkpw(user.getPassword(), result.getPassword());
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean save(User user) {
        try {
            final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            user.setPassword(hashedPassword);

            return UserRepository.saveOrUpdate(user);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean update(User user) {
        String errors = validateUser(user, true);

        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(null, errors, Constants.WARN, JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        try {
            User existingUser = UserRepository.findById(User.class, user.getIdUser());

            if (existingUser != null) {
                existingUser.setName(user.getName());
                existingUser.setUsername(user.getUsername());
                existingUser.setRole(user.getRole());
                existingUser.setBranch(user.getBranch());
                existingUser.setCorporate(user.getCorporate());
                existingUser.setSecretPhrase(user.getSecretPhrase());
                existingUser.setSecretAnswer(user.getSecretAnswer());
                existingUser.setActive(user.getActive());

                String newPassword = user.getPassword();
                if (newPassword != null && !newPassword.isEmpty()) {
                    newPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                    existingUser.setPassword(newPassword);
                }

                return UserRepository.saveOrUpdate(existingUser);
            }
        } catch (
                Exception e) {
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
        } catch (
                Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public static User forgotPassword(User user, String secretAnswer, String newPassword, String repeatedPassword) {
        try {
            if (!secretAnswer.isBlank() && secretAnswer.trim().toLowerCase().replace(" ", "").equals(user.getSecretAnswer())) {
                if (newPassword.equals(repeatedPassword)) {
                    user.setPassword(newPassword);
                    if (update(user)) {
                        return user;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (
                Exception e) {
            Components.showGenericError(null);
        }
        return null;
    }

    public static Role getUserRole(User user) {
        try {
            if (user != null) {
                if (user.getRole().equals('E')) {
                    return new Role(user.getRole(), user, null, Collections.singletonList(user.getBranch()));
                } else {
                    return new Role(user.getRole(), user, UserRepository.getCorporateUser(user), UserRepository.getBranchUser(user));
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> searchByUsernameOrName(String searchText){
        return UserRepository.searchByUsernameOrName(searchText);
    }

    public static List<Corporate> getCorporateUser(User user){
        return UserRepository.getCorporateUser(user);
    }

    public static List<Branch> getBranchUser(User user){
        return UserRepository.getBranchUser(user);
    }

    public static User findByUsername(String username){
        return UserRepository.findByUsername(username);
    }
}
