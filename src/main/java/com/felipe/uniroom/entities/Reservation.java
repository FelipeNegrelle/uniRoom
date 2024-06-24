package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(nullable = false, length = 2)
    private String status = "P";

    @ManyToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_check_in")
    private Date dateTimeCheckIn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_check_out")
    private Date dateTimeCheckOut;

    @Temporal(TemporalType.DATE)
    @Column(name = "initial_date", nullable = false)
    private Date initialDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "final_date", nullable = false)
    private Date finalDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "reservation_guest",
            joinColumns = @JoinColumn(name = "id_reservation"),
            inverseJoinColumns = @JoinColumn(name = "id_guest"))
    private List<Guest> guestList;

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

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public List<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }

    public Reservation() {
    }

    public Reservation(Integer idReservation, Room room, User user, String status, Branch branch, Date dateTimeCheckIn, Date dateTimeCheckOut, Date initialDate, Date finalDate) {
        this.idReservation = idReservation;
        this.room = room;
        this.user = user;
        this.status = status;
        this.branch = branch;
        this.dateTimeCheckIn = dateTimeCheckIn;
        this.dateTimeCheckOut = dateTimeCheckOut;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Reservation))
            return false;
        Reservation that = (Reservation) o;
        return Objects.equals(idReservation, that.idReservation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservation);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", room=" + room +
                ", user=" + user +
                ", status='" + status + '\'' +
                ", branch=" + branch +
                ", dateTimeCheckIn=" + dateTimeCheckIn +
                ", dateTimeCheckOut=" + dateTimeCheckOut +
                ", initialDate=" + initialDate +
                ", finalDate=" + finalDate +
                ", guestList=" + guestList +
                '}';
    }
}
