package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord,String> {
}
