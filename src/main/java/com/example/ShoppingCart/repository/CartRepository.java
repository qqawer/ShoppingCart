package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.CartRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository <CartRecord, String>{
}
