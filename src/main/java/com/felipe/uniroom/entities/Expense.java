package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expense")
    private Integer idExpense;

    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "id_guest", nullable = false)
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "id_item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_service")
    private Service service;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time_expense", nullable = false)
    private Date dateTimeExpense = new Date();

    public Expense() {
    }

    public Expense(Integer idExpense, Reservation reservation, Branch branch, Item item, Service service, Integer amount, Date dateTimeExpense) {
        this.idExpense = idExpense;
        this.reservation = reservation;
        this.branch = branch;
        this.item = item;
        this.service = service;
        this.amount = amount;
        this.dateTimeExpense = dateTimeExpense;
    }

    public Integer getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(Integer idExpense) {
        this.idExpense = idExpense;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getDateTimeExpense() {
        return dateTimeExpense;
    }

    public void setDateTimeExpense(Date dateTimeExpense) {
        this.dateTimeExpense = dateTimeExpense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Expense expense = (Expense) o;
        return idExpense == expense.idExpense && amount == expense.amount && Objects.equals(reservation, expense.reservation) && Objects.equals(branch, expense.branch) && Objects.equals(item, expense.item) && Objects.equals(service, expense.service) && Objects.equals(dateTimeExpense, expense.dateTimeExpense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExpense, reservation, branch, item, service, amount, dateTimeExpense);
    }

    @Override
    public String toString() {
        return "Expense{" + "idExpense=" + idExpense + ", reservation=" + reservation + ", branch=" + branch + ", item=" + item + ", service=" + service + ", amount=" + amount + ", dateTimeExpense=" + dateTimeExpense + '}';
    }
}