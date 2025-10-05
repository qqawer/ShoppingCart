package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord,String> {
    //通过order id 找payment record
    @Query("SELECT o FROM PaymentRecord o WHERE o.order.orderId = :orderId")
    PaymentRecord findByOrderId(String orderId);
}
