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

    @OneToOne
    @JoinColumn(name = "id_corporate")
    private Corporate corporate;

    @OneToOne
    @JoinColumn(name = "id_branch")
    private Branch branch;

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

    public User(Integer idUser, Corporate corporate, Branch branch, String name, String username, String password, Character role, String secretPhrase, String secretAnswer, Boolean active) {
        this.idUser = idUser;
        this.corporate = corporate;
        this.branch = branch;
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

    public Corporate getCorporate() {
        return corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        if (!Objects.equals(idUser, user.idUser))
            return false;
        if (!Objects.equals(name, user.name))
            return false;
        if (!Objects.equals(username, user.username))
            return false;
        if (!Objects.equals(password, user.password))
            return false;
        if (!Objects.equals(role, user.role))
            return false;
        if (!Objects.equals(secretPhrase, user.secretPhrase))
            return false;
        if (!Objects.equals(secretAnswer, user.secretAnswer))
            return false;
        return Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, corporate, branch, name, username, password, role, secretPhrase, secretAnswer, active);
    }
}