package org.example.clientService.models;


import org.example.clientService.util.ClientTypes;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "short_name", unique = true)
    private String shortName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "inn", unique = true)
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "ogrn", unique = true)
    private String ogrn;

    @Enumerated(EnumType.STRING)
    private ClientTypes type;

    @OneToMany(mappedBy = "client")
    private List<Bank> banks;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public ClientTypes getType() {
        return type;
    }

    public void setType(ClientTypes type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return id == client.id && Objects.equals(shortName, client.shortName) && Objects.equals(fullName, client.fullName) && Objects.equals(inn, client.inn) && Objects.equals(kpp, client.kpp) && Objects.equals(ogrn, client.ogrn) && type == client.type && Objects.equals(banks, client.banks) && Objects.equals(createdAt, client.createdAt) && Objects.equals(updatedAt, client.updatedAt) && Objects.equals(createdBy, client.createdBy) && Objects.equals(updatedBy, client.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortName, fullName, inn, kpp, ogrn, type, banks, createdAt, updatedAt, createdBy, updatedBy);
    }
}

