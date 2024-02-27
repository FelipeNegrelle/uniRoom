package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_branch")
    private Integer idBranch;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "id_corporate", nullable = false)
    private Integer idCorporate;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(nullable = false)
    private Boolean active;

    public Branch() {
    }

    public Branch(Integer idBranch, String name, Integer idCorporate, Integer idUser, Boolean active) {
        this.idBranch = idBranch;
        this.name = name;
        this.idCorporate = idCorporate;
        this.idUser = idUser;
        this.active = active;
    }

    public Integer getIdBranch() {
        return this.idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdCorporate() {
        return this.idCorporate;
    }

    public void setIdCorporate(Integer idCorporate) {
        this.idCorporate = idCorporate;
    }

    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
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

    public Branch idBranch(Integer idBranch) {
        setIdBranch(idBranch);
        return this;
    }

    public Branch name(String name) {
        setName(name);
        return this;
    }

    public Branch idCorporate(Integer idCorporate) {
        setIdCorporate(idCorporate);
        return this;
    }

    public Branch idUser(Integer idUser) {
        setIdUser(idUser);
        return this;
    }

    public Branch active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Branch)) {
            return false;
        }
        Branch branch = (Branch) o;
        return Objects.equals(idBranch, branch.idBranch) && Objects.equals(name, branch.name)
                && Objects.equals(idCorporate, branch.idCorporate) && Objects.equals(idUser, branch.idUser)
                && Objects.equals(active, branch.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBranch, name, idCorporate, idUser, active);
    }

    @Override
    public String toString() {
        return "{" +
                " idBranch='" + getIdBranch() + "'" +
                ", name='" + getName() + "'" +
                ", idCorporate='" + getIdCorporate() + "'" +
                ", idUser='" + getIdUser() + "'" +
                ", active='" + isActive() + "'" +
                "}";
    }
}
