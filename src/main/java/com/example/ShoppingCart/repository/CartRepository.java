package com.example.ShoppingCart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShoppingCart.model.CartRecord;

/**
 * Cart Repository - Data access layer for cart operations
 * Extends JpaRepository to provide CRUD operations and custom queries
 */
public interface CartRepository extends JpaRepository <CartRecord, String>{
    // Find all cart records for a specific user
    List<CartRecord> findByUser_UserId(String userId);

    // Find a specific cart record by user ID and product ID (returns Optional)
    Optional<CartRecord> findByUser_UserIdAndProduct_ProductId(String userId, String productId);
}
