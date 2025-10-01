package com.example.ShoppingCart.repository;

import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
