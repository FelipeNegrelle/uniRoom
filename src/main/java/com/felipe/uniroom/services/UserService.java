package com.felipe.uniroom.services;

import com.felipe.uniroom.config.Constants;
import com.felipe.uniroom.entities.Branch;
import com.felipe.uniroom.repositories.BranchRepository;
import com.felipe.uniroom.repositories.UserRepository;
import com.felipe.uniroom.view.Components;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.mindrot.jbcrypt.BCrypt;

import com.felipe.uniroom.entities.User;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
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
    public static Boolean register(User user) {
        try {

            final String validations = validateUser(user, false);
            if (!validations.isEmpty()) {
                JOptionPane.showMessageDialog(null, validations);
                return false;
            } else {
                return BranchRepository.saveOrUpdate(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Components.showGenericError(null);
            return null;
        }
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
                existingUser.setSecretPhrase(user.getSecretPhrase());
                existingUser.setSecretAnswer(user.getSecretAnswer());
                existingUser.setActive(user.getActive());

                String newPassword = user.getPassword();
                if (newPassword != null && !newPassword.isEmpty()) {
                    existingUser.setPassword(newPassword);
                }

                return UserRepository.saveOrUpdate(existingUser);
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
