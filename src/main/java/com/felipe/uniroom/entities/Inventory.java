package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventory")
    private Integer idInventory;

    @OneToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @OneToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column()
    private String description;

    @Column(nullable = false)
    private Boolean active;

    public Integer getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(Integer idInventory) {
        this.idInventory = idInventory;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public Inventory(Room room, Branch branch, String description, Boolean active) {
        this.room = room;
        this.branch = branch;
        this.description = description;
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(idInventory, inventory.idInventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInventory);
    }

    @Override
    public String toString() {
        return "Inventory{" + "idInventory=" + idInventory + ", room=" + room + ", branch=" + branch + ", description='" + description + '\'' + ", active=" + active + '}';
    }

}
