package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findByUser_UserIdAndOrderStatus(String userUserId, Integer orderStatus);

    Order findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(String userUserId, Integer orderStatus);
}
