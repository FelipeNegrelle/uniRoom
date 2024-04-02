package com.felipe.uniroom.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InventoryItemId implements Serializable {
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

}
