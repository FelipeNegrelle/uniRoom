package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public User(Integer idUser, String name, String username, String password, Character role, String secretPhrase,
            String secretAnswer, Boolean active) {
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
        return this.idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getRole() {
        return this.role;
    }

    public void setRole(Character role) {
        this.role = role;
    }

    public String getSecretPhrase() {
        return this.secretPhrase;
    }

    public void setSecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }

    public String getSecretAnswer() {
        return this.secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User idUser(Integer idUser) {
        setIdUser(idUser);
        return this;
    }

    public User name(String name) {
        setName(name);
        return this;
    }

    public User username(String username) {
        setUsername(username);
        return this;
    }

    public User password(String password) {
        setPassword(password);
        return this;
    }

    public User role(Character role) {
        setRole(role);
        return this;
    }

    public User secretPhrase(String secretPhrase) {
        setSecretPhrase(secretPhrase);
        return this;
    }

    public User secretAnswer(String secretAnswer) {
        setSecretAnswer(secretAnswer);
        return this;
    }

    public User active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) && Objects.equals(name, user.name)
                && Objects.equals(username, user.username) && Objects.equals(password, user.password)
                && Objects.equals(role, user.role) && Objects.equals(secretPhrase, user.secretPhrase)
                && Objects.equals(secretAnswer, user.secretAnswer) && Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, name, username, password, role, secretPhrase, secretAnswer, active);
    }

    @Override
    public String toString() {
        return "{" +
                " idUser='" + getIdUser() + "'" +
                ", name='" + getName() + "'" +
                ", username='" + getUsername() + "'" +
                ", password='" + getPassword() + "'" +
                ", role='" + getRole() + "'" +
                ", secretPhrase='" + getSecretPhrase() + "'" +
                ", secretAnswer='" + getSecretAnswer() + "'" +
                ", active='" + isActive() + "'" +
                "}";
    }
}