package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Integer idItem;

    @Column(nullable = false)
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Boolean active;

    // Getters e setters mantidos
    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

    public Item() {
    }

    public Item(Float price, Branch branch, Boolean active) {
        this.price = price;
        this.branch = branch;
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(idItem, item.idItem) && Objects.equals(price, item.price) && Objects.equals(branch, item.branch) && Objects.equals(active, item.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItem, price, branch, active);
    }

    @Override
    public String toString() {
        return "Item{" + "idItem=" + idItem + ", price=" + price + ", branchId=" + branch + ", active=" + active + '}';
    }
}
