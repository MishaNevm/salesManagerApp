package com.example.gatewayService.dto;

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


    private Date created_at;


    private Date updated_at;

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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
