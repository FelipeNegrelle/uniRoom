package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(length = 14, nullable = false)
    private String cnpj;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(nullable = false)
    private Boolean active;

    public Corporate() {
    }

    public Corporate(Integer idCorporate, String name, String cnpj, Integer idUser, Boolean active) {
        this.idCorporate = idCorporate;
        this.name = name;
        this.cnpj = cnpj;
        this.idUser = idUser;
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

    public Corporate idCorporate(Integer idCorporate) {
        setIdCorporate(idCorporate);
        return this;
    }

    public Corporate name(String name) {
        setName(name);
        return this;
    }

    public Corporate cnpj(String cnpj) {
        setCnpj(cnpj);
        return this;
    }

    public Corporate idUser(Integer idUser) {
        setIdUser(idUser);
        return this;
    }

    public Corporate active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Corporate)) {
            return false;
        }
        Corporate corporate = (Corporate) o;
        return Objects.equals(idCorporate, corporate.idCorporate) && Objects.equals(name, corporate.name)
                && Objects.equals(cnpj, corporate.cnpj) && Objects.equals(idUser, corporate.idUser)
                && Objects.equals(active, corporate.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCorporate, name, cnpj, idUser, active);
    }

    @Override
    public String toString() {
        return "{" +
                " idCorporate='" + getIdCorporate() + "'" +
                ", name='" + getName() + "'" +
                ", cnpj='" + getCnpj() + "'" +
                ", idUser='" + getIdUser() + "'" +
                ", active='" + isActive() + "'" +
                "}";
    }
}
