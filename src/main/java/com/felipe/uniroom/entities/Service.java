package com.felipe.uniroom.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "service", uniqueConstraints = {@UniqueConstraint(columnNames = {"description", "id_branch"})})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Integer idService;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private Boolean active = true;

    public Service() {
    }

    public Service(Integer idService, String description, Float price, Branch branch, Boolean active) {
        this.idService = idService;
        this.description = description;
        this.price = price;
        this.branch = branch;
        this.active = active;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Service service = (Service) o;
        return Objects.equals(idService, service.idService) && Objects.equals(description, service.description) && Objects.equals(price, service.price) && Objects.equals(branch, service.branch) && Objects.equals(active, service.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idService, description, price, branch, active);
    }

    @Override
    public String toString() {
        return "Service{" + "idService=" + idService + ", description='" + description + '\'' + ", price=" + price + ", branch=" + branch + ", active=" + active + '}';
    }
}
