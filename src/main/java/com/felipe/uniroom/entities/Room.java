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

    @Column(name = "id_room_type", nullable = false)
    private Integer idRoomType;

    @Column(name = "id_branch", nullable = false)
    private Integer idBranch;

    @Column(nullable = false)
    private Boolean active;

    public Room() {
    }

    public Room(Integer idRoom, Integer roomNumber, Integer idRoomType, Integer idBranch, Boolean active) {
        this.idRoom = idRoom;
        this.roomNumber = roomNumber;
        this.idRoomType = idRoomType;
        this.idBranch = idBranch;
        this.active = active;
    }

    public Integer getIdRoom() {
        return this.idRoom;
    }

    public void setIdRoom(Integer idRoom) {
        this.idRoom = idRoom;
    }

    public Integer getRoomNumber() {
        return this.roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getIdRoomType() {
        return this.idRoomType;
    }

    public void setIdRoomType(Integer idRoomType) {
        this.idRoomType = idRoomType;
    }

    public Integer getIdBranch() {
        return this.idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Room idRoom(Integer idRoom) {
        setIdRoom(idRoom);
        return this;
    }

    public Room roomNumber(Integer roomNumber) {
        setRoomNumber(roomNumber);
        return this;
    }

    public Room idRoomType(Integer idRoomType) {
        setIdRoomType(idRoomType);
        return this;
    }

    public Room idBranch(Integer idBranch) {
        setIdBranch(idBranch);
        return this;
    }

    public Room active(Boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(idRoom, room.idRoom) && Objects.equals(roomNumber, room.roomNumber) && Objects.equals(idRoomType, room.idRoomType) && Objects.equals(idBranch, room.idBranch) && Objects.equals(active, room.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRoom, roomNumber, idRoomType, idBranch, active);
    }

    @Override
    public String toString() {
        return "{" +
            " idRoom='" + getIdRoom() + "'" +
            ", roomNumber='" + getRoomNumber() + "'" +
            ", idRoomType='" + getIdRoomType() + "'" +
            ", idBranch='" + getIdBranch() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
