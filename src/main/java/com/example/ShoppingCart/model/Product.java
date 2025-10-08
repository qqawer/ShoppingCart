package com.example.ShoppingCart.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String productId;

    @Column(length = 200, nullable = false, unique = true)
    private String productName;

    @Column(precision = 10, scale = 2, nullable = false) // Selling price, 10-digit precision, 2 decimal places
    private BigDecimal price;

    @Column(length = 100, nullable = false) // Inventory Quantity
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String mainImage;

    @Column(length = 100)
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer status = 1;  // Status: 0 = Off shelves, 1 = On shelves

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

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public List<CartRecord> getCartRecords() {
        return cartRecords;
    }

    public void setCartRecords(List<CartRecord> cartRecords) {
        this.cartRecords = cartRecords;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartRecord> cartRecords;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}