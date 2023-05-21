package com.example.gatewayService.dto;

//import com.example.salesManagerApp.util.clientUtil.ClientTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class ClientDTO {

    private int id;

    @NotEmpty(message = "Сокращенное наименование должно быть не пустым")
    @Size(min = 2, max = 30, message = "Сокращенное наименование должно быть в диапозоне от 2 до 30")
    private String shortName;

    @NotEmpty(message = "Полное наименование должно быть не пустым")
    @Size(min = 2, max = 50, message = "Полное наименование должно быть в диапозоне от 2 до 50")
    private String fullName;

    @NotEmpty(message = "ИНН должен быть не пустым")
    @Pattern(regexp = "\\d{10}", message = "ИНН должен состоять из 10 цифр")
    private String inn;

    // У ИП кпп отсутвует
    @NotEmpty(message = "КПП должно быть не пустым")
    @Pattern(regexp = "\\d{9}|-", message = "КПП должно сосятоить из 9 цифр, либо '-'")
    private String kpp;

    @NotEmpty(message = "ОГРН должен быть не пустым")
    @Pattern(regexp = "\\d{13}", message = "ОГРН должен состоять из 13 цифр")
    private String ogrn;

//    @NotNull(message = "Тип юридического лица не должен быть пустым")
//    private ClientTypes type;

    private BankDTO bankDTO;

    private Date createdAt;

    private Date updatedAt;

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

//    public ClientTypes getType() {
//        return type;
//    }
//
//    public void setType(ClientTypes type) {
//        this.type = type;
//    }

    public BankDTO getBankDTO() {
        return bankDTO;
    }

    public void setBankDTO(BankDTO bankDTO) {
        this.bankDTO = bankDTO;
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
}
