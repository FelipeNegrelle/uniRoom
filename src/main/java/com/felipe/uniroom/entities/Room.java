package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_room")
    private Integer idRoom;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room_type", nullable = false)
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Boolean active;

    public Room() {
    }

    public Room(Integer idRoom, Integer roomNumber, RoomType roomType, Branch branch, Boolean active) {
        this.idRoom = idRoom;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.branch = branch;
        this.active = active;
    }

    public Integer getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Integer idRoom) {
        this.idRoom = idRoom;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(idRoom, room.idRoom) && Objects.equals(roomNumber, room.roomNumber) && Objects.equals(roomType, room.roomType) && Objects.equals(branch, room.branch) && Objects.equals(active, room.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoom, roomNumber, roomType, branch, active);
    }

    @Override
    public String toString() {
        return "Room{" +
                "idRoom=" + idRoom +
                ", roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", branch=" + branch +
                ", active=" + active +
                '}';
    }
}
