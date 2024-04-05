package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "corporate")
public class Corporate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corporate")
    private Integer idCorporate;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(columnDefinition = "char(14)", nullable = false)
    private String cnpj;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean active;

    public Corporate() {
    }

    public Corporate(Integer idCorporate, String name, String cnpj, User user, Boolean active) {
        this.idCorporate = idCorporate;
        this.name = name;
        this.cnpj = cnpj;
        this.user = user;
        this.active = active;
    }

    public Integer getIdCorporate() {
        return this.idCorporate;
    }

    public void setIdCorporate(Integer idCorporate) {
        this.idCorporate = idCorporate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

        Corporate corporate = (Corporate) o;

        if (!Objects.equals(idCorporate, corporate.idCorporate)) return false;
        if (!Objects.equals(name, corporate.name)) return false;
        if (!Objects.equals(cnpj, corporate.cnpj)) return false;
        if (!Objects.equals(user, corporate.user)) return false;
        return Objects.equals(active, corporate.active);
    }

    @Override
    public int hashCode() {
        int result = idCorporate != null ? idCorporate.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (cnpj != null ? cnpj.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Corporate{" + "idCorporate=" + idCorporate + ", name='" + name + '\'' + ", cnpj='" + cnpj + '\'' + ", user=" + user + ", active=" + active + '}';
    }
}
