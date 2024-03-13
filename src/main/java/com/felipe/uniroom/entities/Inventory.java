package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventory")
    private Integer idInvetory;

    @Column(name = "id_room", nullable = false)
    private Integer idRoom;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    public Integer getIdInvetory() {
        return idInvetory;
    }

    public void setIdInvetory(Integer idInvetory) {
        this.idInvetory = idInvetory;
    }

    public Integer getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Integer idRoom) {
        this.idRoom = idRoom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Inventory() {
    }

    public Inventory(Integer idInvetory, Integer idRoom, String description, Boolean active) {
        this.idInvetory = idInvetory;
        this.idRoom = idRoom;
        this.description = description;
        this.active = active;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Inventory idInvetory(Integer idInvetory) {
        setIdInvetory(idInvetory);
        return this;
    }

    public Inventory idRoom(Integer idRoom) {
        setIdRoom(idRoom);
        return this;
    }

    public Inventory description(String description) {
        setDescription(description);
        return this;
    }

    public Inventory active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Inventory)) {
            return false;
        }
        Inventory inventory = (Inventory) o;
        return Objects.equals(idInvetory, inventory.idInvetory) && Objects.equals(idRoom, inventory.idRoom)
                && Objects.equals(description, inventory.description) && Objects.equals(active, inventory.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvetory, idRoom, description, active);
    }

    @Override
    public String toString() {
        return "{" +
                " idInvetory='" + getIdInvetory() + "'" +
                ", idRoom='" + getIdRoom() + "'" +
                ", description='" + getDescription() + "'" +
                ", active='" + isActive() + "'" +
                "}";
    }

}
