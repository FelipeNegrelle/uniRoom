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

    @OneToOne()
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Short days;

    @OneToOne()
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false, length = 2)
    private String status;

    @ManyToOne()
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Short getDays() {
        return days;
    }

    public void setDays(Short days) {
        this.days = days;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Reservation() {
    }

    public Reservation(Room room, Short days, User user, String status, Branch branch) {
        this.room = room;
        this.days = days;
        this.user = user;
        this.status = status;
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(idReservation, that.idReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation);
    }

    @Override
    public String toString() {
        return "Reservation{" + "idReservation=" + idReservation + ", room=" + room + ", days=" + days + ", user=" + user + ", status='" + status + '\'' + ", branch=" + branch + '}';
    }
}
