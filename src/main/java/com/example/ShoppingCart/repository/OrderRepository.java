package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,String> {

}
