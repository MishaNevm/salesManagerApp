package com.example.Frontend.dto;




import com.example.Frontend.util.ProductTypes;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class ProductDTO {
    private int id;

    @NotEmpty(message = "Наименование товара не должно быть пустым")
    private String name;

    private ProductTypes type;

    @Min(value = 0, message = "Количество товара не может быть меньше 0")
    private int quantity;

    private Date createdAt;

    private Date updatedAt;

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

    public ProductTypes getType() {
        return type;
    }

    public void setType(ProductTypes type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
