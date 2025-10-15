package com.example.ShoppingCart.pojo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductCreateDTO {

    @NotBlank(message = "Product name cannot be empty")
    private String productName;


    @NotNull(message = "Product price cannot be empty")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;


    @NotNull(message = "Product stock cannot be empty")
    @PositiveOrZero(message = "Product stock cannot be negative")
    private Integer stock;


    private String brand;


    private String mainImage;

    private String description;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // status: non-required, default 1 (on the shelf), here add verification to ensure that the value can only be 0 or 1
    @PositiveOrZero(message = "Status value is invalid")
    private Integer status = 1;
}
