package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @Column(name = "id_room", nullable = true)
    private Integer idRoom;

    @Column(nullable = false)
    private Short days;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(nullable = false, length = 2)
    private String status;

    @Column(name = "id_branch", nullable = false)
    private Integer idBranch;

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public Integer getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Integer idRoom) {
        this.idRoom = idRoom;
    }

    public Short getDays() {
        return days;
    }

    public void setDays(Short days) {
        this.days = days;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public Reservation() {
    }

    public Reservation(Integer idReservation, Integer idRoom, Short days, Integer idUser, String status,
            Integer idBranch) {
        this.idReservation = idReservation;
        this.idRoom = idRoom;
        this.days = days;
        this.idUser = idUser;
        this.status = status;
        this.idBranch = idBranch;
    }

    public Reservation idReservation(Integer idReservation) {
        setIdReservation(idReservation);
        return this;
    }

    public Reservation idRoom(Integer idRoom) {
        setIdRoom(idRoom);
        return this;
    }

    public Reservation days(Short days) {
        setDays(days);
        return this;
    }

    public Reservation idUser(Integer idUser) {
        setIdUser(idUser);
        return this;
    }

    public Reservation status(String status) {
        setStatus(status);
        return this;
    }

    public Reservation idBranch(Integer idBranch) {
        setIdBranch(idBranch);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation reservation = (Reservation) o;
        return Objects.equals(idReservation, reservation.idReservation) && Objects.equals(idRoom, reservation.idRoom)
                && Objects.equals(days, reservation.days) && Objects.equals(idUser, reservation.idUser)
                && Objects.equals(status, reservation.status) && Objects.equals(idBranch, reservation.idBranch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation, idRoom, days, idUser, status, idBranch);
    }

    @Override
    public String toString() {
        return "{" +
                " idReservation='" + getIdReservation() + "'" +
                ", idRoom='" + getIdRoom() + "'" +
                ", days='" + getDays() + "'" +
                ", idUser='" + getIdUser() + "'" +
                ", status='" + getStatus() + "'" +
                ", idBranch='" + getIdBranch() + "'" +
                "}";
    }
}
