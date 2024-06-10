package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @ManyToOne()
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Short days;

    @OneToOne()
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false, length = 2)
    private String status = "CI";

    @ManyToOne()
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_check_in", nullable = false)
    private Date dateTimeCheckIn = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_check_out")
    private Date dateTimeCheckOut;

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

    public Date getDateTimeCheckIn() {
        return dateTimeCheckIn;
    }

    public void setDateTimeCheckIn(Date dateTimeCheckIn) {
        this.dateTimeCheckIn = dateTimeCheckIn;
    }

    public Date getDateTimeCheckOut() {
        return dateTimeCheckOut;
    }

    public void setDateTimeCheckOut(Date dateTimeCheckOut) {
        this.dateTimeCheckOut = dateTimeCheckOut;
    }

    public Reservation() {
    }

    public Reservation(Integer idReservation, Room room, Short days, User user, String status, Branch branch, Date dateTimeCheckIn, Date dateTimeCheckOut) {
        this.idReservation = idReservation;
        this.room = room;
        this.days = days;
        this.user = user;
        this.status = status;
        this.branch = branch;
        this.dateTimeCheckIn = dateTimeCheckIn;
        this.dateTimeCheckOut = dateTimeCheckOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Reservation that))
            return false;
        return Objects.equals(idReservation, that.idReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation);
    }

    @Override
    public String toString() {
        return "Reservation{" + "idReservation=" + idReservation + ", room=" + room + ", days=" + days + ", user=" + user + ", status='" + status + '\'' + ", branch=" + branch + ", dateTimeCheckIn=" + dateTimeCheckIn + ", dateTimeCheckOut=" + dateTimeCheckOut + '}';
    }
}
