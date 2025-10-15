package com.example.ShoppingCart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShoppingCart.model.CartRecord;

public interface CartRepository extends JpaRepository <CartRecord, String>{
    //find the all CartRecord by UserId
    List<CartRecord> findByUser_UserId(String userId);
    //find the one CartRecord by userId and productId
    Optional<CartRecord> findByUser_UserIdAndProduct_ProductId(String userId, String productId);
}
