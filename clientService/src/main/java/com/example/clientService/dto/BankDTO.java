package com.example.clientService.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class BankDTO {

    private int id;

    @NotEmpty(message = "Наименование банка должно быть не пустым")
    @Size(min = 2, max = 90, message = "Наименование банка должно быть от 2 до 90 символов")
    private String name;

    @NotEmpty(message = "БИК должен быть не пустым")
    @Pattern(regexp = "\\d{9}", message = "БИК должен состоять из 9 цифр")
    private String bik;

    @NotEmpty(message = "Рассчетный счет должен быть не пустым")
    @Pattern(regexp = "\\d{20}", message = "Рассчетный счет должен состоять из 20 цирф")
    private String checkingAccount;

    @NotEmpty(message = "Корпоративный счет должен быть не пустым")
    @Pattern(regexp = "\\d{20}", message = "Корпоративный счет должен состоять из 20 цифр")
    private String corporateAccount;

    @NotEmpty(message = "Город должен быть не пустым")
    @Pattern(regexp = "[А-Я][а-я]+", message = "Город должен называться с большой буквы")
    @Size(min = 2, max = 50, message = "Город должен быть от 2 до 50 символов")
    private String city;

    @NotEmpty(message = "Страна должна быть не пустой")
    @Pattern(regexp = "[А-Я][а-я]+", message = "Страна должна называться с большой буквы")
    @Size(min = 2, max = 50, message = "Страна должна быть от 2 до 50 символов")
    private String country;

    private ClientDTO clientDTO;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

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


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
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
}
