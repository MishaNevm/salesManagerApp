package org.example.clientService.models;


import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "bik", unique = true)
    private String bik;

    @Column(name = "checking_account", unique = true)
    private String checkingAccount;

    @Column(name = "corporate_account")
    private String corporateAccount;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBik() {
        return bik;
    }

    public void setBik(String bik) {
        this.bik = bik;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public String getCorporateAccount() {
        return corporateAccount;
    }

    public void setCorporateAccount(String corporateAccount) {
        this.corporateAccount = corporateAccount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public void setUpdatedAt(Date updated_at) {
        this.updatedAt = updated_at;
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
        if (!(o instanceof Bank bank)) return false;
        return id == bank.id && Objects.equals(name, bank.name) && Objects.equals(bik, bank.bik) && Objects.equals(checkingAccount, bank.checkingAccount) && Objects.equals(corporateAccount, bank.corporateAccount) && Objects.equals(city, bank.city) && Objects.equals(country, bank.country) && Objects.equals(client, bank.client) && Objects.equals(createdAt, bank.createdAt) && Objects.equals(updatedAt, bank.updatedAt) && Objects.equals(createdBy, bank.createdBy) && Objects.equals(updatedBy, bank.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bik, checkingAccount, corporateAccount, city, country, client, createdAt, updatedAt, createdBy, updatedBy);
    }
}
