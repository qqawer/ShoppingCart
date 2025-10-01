package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
