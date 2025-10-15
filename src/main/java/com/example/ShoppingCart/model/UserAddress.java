package com.example.ShoppingCart.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String addressId;

    @Column(length = 50)
    private String receiverName;

    @Column(length = 20)
    private String phone;

    @Column(length = 100) // Province-City-District, e.g., 'Beijing-Chaoyang-Sanlitun'
    private String region;

    @Column(length = 200) // Detailed address, such as 'Building 1, Unit 1, Apartment 101, XX Community'
    private String detailAddress;

    private Boolean isDefault = false; // Is it the default address: true = yes, false = no

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @ManyToOne(optional = false) // optional=false, Indicates that this foreign key must be provided
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Order> orders;
}