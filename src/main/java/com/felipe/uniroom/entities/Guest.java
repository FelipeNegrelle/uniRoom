package com.felipe.uniroom.entities;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "guest")
public class    Guest {
    @Id
    @Column(name = "id_guest")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGuest;

    @Column(nullable = false, length = 50)
    private String name;

    @CPF
    @Column(columnDefinition = "char(11)")
    private String cpf;

    @Column(name = "is_foreigner", nullable = false)
    private Boolean isForeigner;

    @Column(name = "passport_number", length = 9)
    private String passportNumber;

    @OneToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @ManyToMany(mappedBy = "guestList")
    private List<Reservation> reservationList;

    public Guest() {
    }

    public Guest(Integer idGuest, String name, String cpf, Boolean isForeigner, String passportNumber, Branch branch) {
        this.idGuest = idGuest;
        this.name = name;
        this.cpf = cpf;
        this.isForeigner = isForeigner;
        this.passportNumber = passportNumber;
        this.branch = branch;
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

    public Boolean getIsForeigner() {
        return isForeigner;
    }

    public void setIsForeigner(Boolean isForeigner) {
        this.isForeigner = isForeigner;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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
        return Objects.equals(idGuest, guest.idGuest) &&
                Objects.equals(name, guest.name) &&
                Objects.equals(cpf, guest.cpf) &&
                Objects.equals(isForeigner, guest.isForeigner) &&
                Objects.equals(passportNumber, guest.passportNumber) &&
                Objects.equals(branch, guest.branch);
    }


    @Override
    public String toString() {
        return "Guest{" +
                "idGuest=" + idGuest +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", isForeigner=" + isForeigner +
                ", passportNumber='" + passportNumber + '\'' +
                ", branch=" + branch +
                '}';
    }
}
