package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reservation_movement")
public class ReservationMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_movement")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "id_expense")
    private Expense expense;

    @Column(nullable = false)
    private Double value;

    @Column(length = 100)
    private String description;

    @Column(name = "movement_type", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String movementType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_movement", nullable = false)
    private Date dateTimeMovement = new Date();

    public ReservationMovement() {
    }

    public ReservationMovement(Integer id, Reservation reservation, Double value, String description, String movementType, Date dateTimeMovement, Expense expense) {
        this.id = id;
        this.reservation = reservation;
        this.value = value;
        this.description = description;
        this.movementType = movementType;
        this.dateTimeMovement = dateTimeMovement;
        this.expense = expense;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Date getDateTimeMovement() {
        return dateTimeMovement;
    }

    public void setDateTimeMovement(Date dateTimeMovement) {
        this.dateTimeMovement = dateTimeMovement;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "ReservationMovement{" + "id=" + id + ", reservation=" + reservation + ", expense=" + expense + ", value=" + value + ", description='" + description + '\'' + ", movementType='" + movementType + '\'' + ", dateTimeMovement=" + dateTimeMovement + '}';
    }
}