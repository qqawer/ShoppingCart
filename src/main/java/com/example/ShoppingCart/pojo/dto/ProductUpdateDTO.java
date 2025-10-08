package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ProductUpdateDTO {

    // 编辑必须传id，用于定位产品（必传，非空且为正数）
    @NotNull(message = "产品ID不能为空")
    private String productId;

    private String productName;

    // 价格：编辑时可选，传值则必须>0
    @DecimalMin(value = "0.01", message = "产品价格必须大于0")
    private BigDecimal price;

    @PositiveOrZero(message = "产品库存不能为负数")
    private Integer stock;

    private String brand;

    private String mainImage;

    private String description;

    @PositiveOrZero(message = "状态值无效")
    private Integer status;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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
}