package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "inventory_item")
public class InvetoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventory")
    private Integer idInvetory;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(nullable = false)
    private Integer quantity;

    public Integer getIdInvetory() {
        return idInvetory;
    }

    public void setIdInvetory(Integer idInvetory) {
        this.idInvetory = idInvetory;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public InvetoryItem() {
    }

    public InvetoryItem(Integer idInvetory, Integer idItem, Integer quantity) {
        this.idInvetory = idInvetory;
        this.idItem = idItem;
        this.quantity = quantity;
    }

    public InvetoryItem idInvetory(Integer idInvetory) {
        setIdInvetory(idInvetory);
        return this;
    }

    public InvetoryItem idItem(Integer idItem) {
        setIdItem(idItem);
        return this;
    }

    public InvetoryItem quantity(Integer quantity) {
        setQuantity(quantity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof InvetoryItem)) {
            return false;
        }
        InvetoryItem invetoryItem = (InvetoryItem) o;
        return Objects.equals(idInvetory, invetoryItem.idInvetory) && Objects.equals(idItem, invetoryItem.idItem)
                && Objects.equals(quantity, invetoryItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvetory, idItem, quantity);
    }

    @Override
    public String toString() {
        return "{" +
                " idInvetory='" + getIdInvetory() + "'" +
                ", idItem='" + getIdItem() + "'" +
                ", quantity='" + getQuantity() + "'" +
                "}";
    }

}
