package com.felipe.uniroom.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class InvetoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invetory")
    private Integer idInvetory;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(name ="quantity", nullable = false)
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
