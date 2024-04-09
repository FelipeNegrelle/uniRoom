package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private Character role;

    @Column(name = "secret_phrase", nullable = false)
    private String secretPhrase;

    @Column(name = "secret_answer", nullable = false)
    private String secretAnswer;

    @Column(nullable = false)
    private Boolean active;

    public User() {
    }

    public User(Integer idUser, String name, String username, String password, Character role, String secretPhrase, String secretAnswer, Boolean active) {
        this.idUser = idUser;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.secretPhrase = secretPhrase;
        this.secretAnswer = secretAnswer;
        this.active = active;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getRole() {
        return role;
    }

    public void setRole(Character role) {
        this.role = role;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }

    public void setSecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!Objects.equals(idUser, user.idUser)) return false;
        if (!Objects.equals(name, user.name)) return false;
        if (!Objects.equals(username, user.username)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(role, user.role)) return false;
        if (!Objects.equals(secretPhrase, user.secretPhrase)) return false;
        if (!Objects.equals(secretAnswer, user.secretAnswer)) return false;
        return Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (secretPhrase != null ? secretPhrase.hashCode() : 0);
        result = 31 * result + (secretAnswer != null ? secretAnswer.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }
}