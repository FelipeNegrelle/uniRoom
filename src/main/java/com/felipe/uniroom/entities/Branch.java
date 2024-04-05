package com.felipe.uniroom.entities;

import jakarta.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_corporate", nullable = false)
    private Corporate corporate;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(nullable = false)
    private Boolean active;

    public Branch() {
    }

    public Branch(Integer idBranch, String name, Corporate corporate, Integer idUser, Boolean active) {
        this.idBranch = idBranch;
        this.name = name;
        this.corporate = corporate;
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

    public Corporate getCorporate() {
        return this.corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Branch branch = (Branch) o;

        if (!Objects.equals(idBranch, branch.idBranch)) return false;
        if (!Objects.equals(name, branch.name)) return false;
        if (!Objects.equals(corporate, branch.corporate)) return false;
        if (!Objects.equals(idUser, branch.idUser)) return false;
        return Objects.equals(active, branch.active);
    }

    @Override
    public int hashCode() {
        int result = idBranch != null ? idBranch.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (corporate != null ? corporate.hashCode() : 0);
        result = 31 * result + (idUser != null ? idUser.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Branch{" + "idBranch=" + idBranch + ", name='" + name + '\'' + ", corporate=" + corporate + ", idUser=" + idUser + ", active=" + active + '}';
    }
}
