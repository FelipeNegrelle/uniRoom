package com.felipe.uniroom.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InventoryItemId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id_inventory")
    private Integer idInvetory;

    @Column(name = "id_item")
    private Integer idItem;

    public Integer getIdInvetory() {
        return this.idInvetory;
    }

    public void setIdInvetory(Integer idInvetory) {
        this.idInvetory = idInvetory;
    }

    public Integer getIdItem() {
        return this.idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryItemId that = (InventoryItemId) o;

        if (!Objects.equals(idInvetory, that.idInvetory)) return false;
        return Objects.equals(idItem, that.idItem);
    }

    @Override
    public int hashCode() {
        int result = idInvetory != null ? idInvetory.hashCode() : 0;
        result = 31 * result + (idItem != null ? idItem.hashCode() : 0);
        return result;
    }
}
