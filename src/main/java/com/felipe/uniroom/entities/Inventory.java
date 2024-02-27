package com.felipe.uniroom.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invetory")
    private Integer idInvetory;

    @Column(name ="id_room", nullable = false)
    private Integer idRoom;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 1)
    private Integer active;

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

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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
