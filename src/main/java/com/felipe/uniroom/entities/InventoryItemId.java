package com.felipe.uniroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InventoryItemId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "id_inventory")
    private Integer idInvetory;

    @Column(name = "id_item")
    private Integer idItem;

    public InventoryItemId(){}

    public InventoryItemId(Integer idInvetory, Integer idItem) {
        this.idInvetory = idInvetory;
        this.idItem = idItem;
    }

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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InventoryItemId that = (InventoryItemId) o;

        if (!Objects.equals(idInvetory, that.idInvetory))
            return false;
        return Objects.equals(idItem, that.idItem);
    }

    @Override
    public int hashCode() {
        int result = idInvetory != null ? idInvetory.hashCode() : 0;
        result = 31 * result + (idItem != null ? idItem.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InventoryItemId{" + "idInvetory=" + idInvetory + ", idItem=" + idItem + '}';
    }
}
