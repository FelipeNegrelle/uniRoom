package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room_type")
    private Integer idRoomType;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Byte capacity;

    @Column(nullable = false)
    private Float price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Boolean active;

    public Integer getIdRoomType() {
        return idRoomType;
    }

    public void setIdRoomType(Integer idRoomType) {
        this.idRoomType = idRoomType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getCapacity() {
        return capacity;
    }

    public void setCapacity(Byte capacity) {
        this.capacity = capacity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RoomType() {
    }

    public RoomType(Integer idRoomType, String name, Byte capacity, Float price, Branch branch, Boolean active) {
        this.idRoomType = idRoomType;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.branch = branch;
        this.active = active;
    }

    public Boolean getActive() {
        return this.active;
    }

    public RoomType idRoomType(Integer idRoomType) {
        setIdRoomType(idRoomType);
        return this;
    }

    public RoomType name(String name) {
        setName(name);
        return this;
    }

    public RoomType capacity(Byte capacity) {
        setCapacity(capacity);
        return this;
    }

    public RoomType price(Float price) {
        setPrice(price);
        return this;
    }

    public RoomType idBranch(Branch branch) {
        setBranch(branch);
        return this;
    }

    public RoomType active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RoomType)) {
            return false;
        }
        RoomType roomType = (RoomType) o;
        return Objects.equals(idRoomType, roomType.idRoomType) && Objects.equals(name, roomType.name)
                && Objects.equals(capacity, roomType.capacity) && Objects.equals(price, roomType.price)
                && Objects.equals(branch, roomType.branch) && Objects.equals(active, roomType.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoomType, name, capacity, price, branch, active);
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "idRoomType=" + idRoomType +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", price=" + price +
                ", branch=" + branch +
                ", active=" + active +
                '}';
    }
}
