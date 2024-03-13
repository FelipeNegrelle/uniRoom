package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(nullable = false)
    private Float price;

    @Column(name = "id_branch", nullable = false)
    private Integer idBranch;

    @Column(nullable = false)
    private Boolean active;

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Item() {
    }

    public Item(Integer idItem, Float price, Integer idBranch, Boolean active) {
        this.idItem = idItem;
        this.price = price;
        this.idBranch = idBranch;
        this.active = active;
    }

    public Item idItem(Integer idItem) {
        setIdItem(idItem);
        return this;
    }

    public Item price(Float price) {
        setPrice(price);
        return this;
    }

    public Item idBranch(Integer idBranch) {
        setIdBranch(idBranch);
        return this;
    }

    public Item active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(idItem, item.idItem) && Objects.equals(price, item.price)
                && Objects.equals(idBranch, item.idBranch) && Objects.equals(active, item.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItem, price, idBranch, active);
    }

    @Override
    public String toString() {
        return "{" +
                " idItem='" + getIdItem() + "'" +
                ", price='" + getPrice() + "'" +
                ", idBranch='" + getIdBranch() + "'" +
                ", active='" + getActive() + "'" +
                "}";
    }
}
