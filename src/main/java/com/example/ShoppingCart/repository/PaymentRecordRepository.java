package com.example.ShoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ShoppingCart.model.PaymentRecord;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord,String> {
    //find payment record by order id
    @Query("SELECT o FROM PaymentRecord o WHERE o.order.orderId = :orderId")
    PaymentRecord findByOrderId(String orderId);
}
