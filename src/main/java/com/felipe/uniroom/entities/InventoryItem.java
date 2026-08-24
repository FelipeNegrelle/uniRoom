package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "inventory_item")
public class InventoryItem implements Serializable {
    @EmbeddedId
    private InventoryItemId id;

    @Column(nullable = false)
    private Integer quantity;

    public InventoryItem() {
    }

    public InventoryItem(InventoryItemId id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public InventoryItemId getId() {
        return this.id;
    }

    public void setId(InventoryItemId id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof InventoryItem)) {
            return false;
        }
        InventoryItem invetoryItem = (InventoryItem) o;
        return Objects.equals(id, invetoryItem.id) && Objects.equals(quantity, invetoryItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }

    @Override
    public String toString() {
        return "InvetoryItem{" + "id=" + id + ", quantity=" + quantity + '}';
    }
}
