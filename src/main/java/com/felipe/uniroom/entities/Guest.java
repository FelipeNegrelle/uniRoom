package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "guest")
public class Guest {
    @Id
    @Column(name = "id_guest")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGuest;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @CPF
    @Column(columnDefinition = "char(11)", nullable = false, unique = true)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_room")
    private Room room;

    @OneToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column
    private Boolean hosted = false;

    @ManyToMany(mappedBy = "guestList")
    private List<Reservation> reservationList;

    public Guest() {
    }

    public Guest(Integer idGuest, String name, String cpf, Room room, Branch branch, Boolean hosted) {
        this.idGuest = idGuest;
        this.name = name;
        this.cpf = cpf;
        this.room = room;
        this.branch = branch;
        this.hosted = hosted;
    }

    public Integer getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(Integer idGuest) {
        this.idGuest = idGuest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @CPF String getCpf() {
        return cpf;
    }

    public void setCpf(@CPF String cpf) {
        this.cpf = cpf;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Boolean getHosted() {
        return hosted;
    }

    public void setHosted(Boolean hosted) {
        this.hosted = hosted;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Guest guest = (Guest) o;
        return idGuest == guest.idGuest && Objects.equals(name, guest.name) && Objects.equals(cpf, guest.cpf) && Objects.equals(room, guest.room) && Objects.equals(hosted, guest.hosted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGuest, name, cpf, room, branch, hosted);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "idGuest=" + idGuest +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", room=" + room +
                ", branch=" + branch +
                ", hosted=" + hosted +
                '}';
    }
}
