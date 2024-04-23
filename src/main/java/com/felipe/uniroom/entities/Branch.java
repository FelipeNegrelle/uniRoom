package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CNPJ;

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

    @OneToOne()
    @JoinColumn(name = "id_corporate", nullable = false)
    private Corporate corporate;

    @OneToOne()
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @CNPJ
    @Column(columnDefinition = "char(14)", nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private Boolean active;

    public Branch() {
    }

    public Branch(Integer idBranch, String name, Corporate corporate, User user, String cnpj, Boolean active) {
        this.idBranch = idBranch;
        this.name = name;
        this.corporate = corporate;
        this.user = user;
        this.cnpj = cnpj;
        this.active = active;
    }

    public Integer getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Corporate getCorporate() {
        return corporate;
    }

    public void setCorporate(Corporate corporate) {
        this.corporate = corporate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

        Branch branch = (Branch) o;

        if (!Objects.equals(idBranch, branch.idBranch)) return false;
        if (!Objects.equals(name, branch.name)) return false;
        if (!Objects.equals(corporate, branch.corporate)) return false;
        if (!Objects.equals(user, branch.user)) return false;
        if (!Objects.equals(cnpj, branch.cnpj)) return false;
        return Objects.equals(active, branch.active);
    }

    @Override
    public int hashCode() {
        int result = idBranch != null ? idBranch.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (corporate != null ? corporate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (cnpj != null ? cnpj.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }
}
