package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.CartRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository <CartRecord, String>{

    List<CartRecord> findByUser_UserId(String userId);

}
