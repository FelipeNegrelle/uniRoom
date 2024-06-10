package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CNPJ;

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

    @CNPJ
    @Column(columnDefinition = "char(14)", nullable = false)
    private String cnpj;

    @OneToOne()
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Corporate corporate = (Corporate) o;
        return Objects.equals(idCorporate, corporate.idCorporate) && Objects.equals(name, corporate.name) && Objects.equals(cnpj, corporate.cnpj) && Objects.equals(user, corporate.user) && Objects.equals(active, corporate.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorporate, name, cnpj, user, active);
    }

    @Override
    public String toString() {
        return "Corporate{" + "idCorporate=" + idCorporate + ", name='" + name + '\'' + ", cnpj='" + cnpj + '\'' + ", user=" + user + ", active=" + active + '}';
    }
}
