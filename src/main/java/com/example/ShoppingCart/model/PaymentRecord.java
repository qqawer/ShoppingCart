package com.example.ShoppingCart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String paymentId;

    @Column(length = 20) //// Payment methods: Alipay, wechat Pay, UnionPay
    private String paymentMethod;

    @Column(precision = 10, scale = 2) // Payment amount (should be consistent with the total order amount)
    private BigDecimal paymentAmount;

    @CreationTimestamp // Automatically fill the payment time
    @Column(updatable = false)
    private LocalDateTime paymentTime;

    @Column(length = 100) // Payment platform transaction number (such as Alipay's out_trade_no)
    private String tradeNo;

    private Integer payStatus = 0;// Payment status: 0= Pending payment, 1= Paid, 2= Payment Failed

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id",unique = true)// unique=true ensures that there is only one payment record for each order
    private Order order;
}